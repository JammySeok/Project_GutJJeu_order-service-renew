import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// 실행 옵션 (-e 로 전달)
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const MODE = __ENV.MODE || 'consistency';                      // consistency | performance
const API_PATH = __ENV.API_PATH || '/test/stocks/deduct';      // /test/stocks/deduct | /test/stocks/deduct-legacy
const ITEMS = parseInt(__ENV.ITEMS || '3');                    // 성능 테스트: 주문 1건의 상품 수

// 결과 집계용 카운터
const orderSuccess = new Counter('order_success');      // 200 차감 성공
const orderSoldOut = new Counter('order_sold_out');     // 409 OUT_OF_STOCK (정상적인 품절)
const orderError = new Counter('order_error');          // 그 외 (진짜 에러)

// 200과 409(품절)은 "예상된 응답" → http_req_failed에 포함하지 않음
http.setResponseCallback(http.expectedStatuses(200, 409));

const scenarios = {
    // 정합성: 100명이 총 1,000건을 나눠서 최대한 빠르게 요청
    consistency: {
        executor: 'shared-iterations',
        vus: 100,
        iterations: 1000,
        maxDuration: '2m',
    },
    // 성능: 100명이 30초 동안 쉬지 않고 요청
    performance: {
        executor: 'constant-vus',
        vus: 100,
        duration: '30s',
    },
};

export const options = {
    scenarios: { deduct: scenarios[MODE] },
    summaryTrendStats: ['avg', 'min', 'med', 'p(90)', 'p(95)', 'p(99)', 'max'],
};

// 옵션 코드 생성: 1 → OPT001
const code = (n) => 'OPT' + String(n).padStart(3, '0');

// 정합성: OPT003만 2개씩 → 재고 100이면 정확히 50건만 성공해야 함
const consistencyItems = [
    { productCode: 'P001', optionCode: 'OPT001', quantity: 1 },
    { productCode: 'P001', optionCode: 'OPT002', quantity: 1 },
    { productCode: 'P001', optionCode: 'OPT003', quantity: 2 },
];

// 성능: OPT001 ~ OPT{ITEMS} 를 1개씩
const performanceItems = Array.from({ length: ITEMS }, (_, i) => ({
    productCode: 'P001',
    optionCode: code(i + 1),
    quantity: 1,
}));

const payload = JSON.stringify(MODE === 'consistency' ? consistencyItems : performanceItems);

const params = {
    headers: { 'Content-Type': 'application/json' },
};

export default function () {
    const res = http.post(`${BASE_URL}${API_PATH}`, payload, params);

    const soldOut = res.status === 409 && res.body && res.body.includes('OUT_OF_STOCK');

    if (res.status === 200) orderSuccess.add(1);
    else if (soldOut) orderSoldOut.add(1);
    else orderError.add(1);

    check(res, { '차감 성공 또는 정상 품절': () => res.status === 200 || soldOut });
}