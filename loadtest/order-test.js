import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// 실행 옵션 (-e 로 전달)
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const MODE = __ENV.MODE || 'consistency';      // consistency | performance

// 결과 집계용 카운터
const orderSuccess = new Counter('order_success');      // 201 주문 성공
const orderSoldOut = new Counter('order_sold_out');     // 400 OUT_OF_STOCK (정상적인 품절)
const orderError = new Counter('order_error');          // 그 외 (진짜 에러)

// 201과 400(품절)은 "예상된 응답" → http_req_failed에 포함하지 않음
http.setResponseCallback(http.expectedStatuses(201, 400));

const scenarios = {
    // ① 정합성: 100명이 총 1,000건을 나눠서 최대한 빠르게 요청
    consistency: {
        executor: 'shared-iterations',
        vus: 100,
        iterations: 1000,
        maxDuration: '2m',
    },
    // ② 성능: 100명이 30초 동안 쉬지 않고 요청
    performance: {
        executor: 'constant-vus',
        vus: 100,
        duration: '30s',
    },
};

export const options = {
    scenarios: { order: scenarios[MODE] },
    summaryTrendStats: ['avg', 'min', 'med', 'p(90)', 'p(95)', 'p(99)', 'max'],
};

const payload = JSON.stringify({
    orderType: 'DIRECT',
    productInfo: { productCode: 'P001', optionCode: 'OPT001', quantity: 1 },
});

const params = {
    headers: { 'Content-Type': 'application/json', 'X-USERS-IDX': '1' },
};

export default function () {
    const res = http.post(`${BASE_URL}/orders`, payload, params);

    const soldOut = res.status === 400 && res.body && res.body.includes('OUT_OF_STOCK');

    if (res.status === 201) orderSuccess.add(1);
    else if (soldOut) orderSoldOut.add(1);
    else orderError.add(1);

    check(res, { '주문 성공 또는 정상 품절': () => res.status === 201 || soldOut });
}