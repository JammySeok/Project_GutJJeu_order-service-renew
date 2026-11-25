import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// 실행 옵션 (-e 로 전달)
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const CALLS = Number(__ENV.CALLS || 10);         // 같은 주문에 보낼 결제 성공 호출 수
const PAYMENT_IDX = __ENV.PAYMENT_IDX || '1';    // 정산 행 수를 셀 때 쓰는 결제 번호

// 결과 집계용 카운터
const successOk = new Counter('success_ok');        // 200 응답
const successError = new Counter('success_error');  // 그 외 응답

export const options = {
    scenarios: {
        duplicate: {
            executor: 'per-vu-iterations',  // VU마다 정확히 1번씩 → 총 CALLS번 동시 호출
            vus: CALLS,
            iterations: 1,
            maxDuration: '2m',
        },
    },
    summaryTrendStats: ['avg', 'min', 'med', 'p(90)', 'p(95)', 'p(99)', 'max'],
};

const params = {
    headers: { 'Content-Type': 'application/json' },
};

// 테스트 시작 전 1번만 실행: 주문을 하나 만들고 orderCode를 VU들에게 넘김
export function setup() {
    const orderPayload = JSON.stringify({
        orderType: 'DIRECT',
        productInfo: { productCode: 'P001', optionCode: 'OPT001', quantity: 1 },
    });

    const res = http.post(`${BASE_URL}/orders`, orderPayload, params);

    if (res.status !== 201) {
        throw new Error(`주문 생성 실패: status=${res.status}, body=${res.body}`);
    }

    const orderCode = res.json('data.orderCode');
    console.log(`생성된 orderCode: ${orderCode}`);

    return { orderCode };
}

// 모든 VU가 같은 orderCode로 결제 성공 호출
export default function (data) {
    const successPayload = JSON.stringify({
        addressInfo: {
            recipient: '홍길동',
            address: '서울시 강남구',
            addressDetail: '101호',
            phone: '010-1234-5678',
        },
    });

    const res = http.post(
        `${BASE_URL}/client/orders/success/${data.orderCode}/${PAYMENT_IDX}`,
        successPayload,
        params
    );

    if (res.status === 200) successOk.add(1);
    else successError.add(1);

    check(res, { '결제 후처리 응답 200': (r) => r.status === 200 });
}