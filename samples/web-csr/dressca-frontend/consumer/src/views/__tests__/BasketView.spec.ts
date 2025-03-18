import { describe, it, expect, vi, beforeAll, assert } from 'vitest';
import { flushPromises, mount, VueWrapper } from '@vue/test-utils';
import { router } from '@/router';
import { i18n } from '@/locales/i18n';
import { createTestingPinia } from '@pinia/testing';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import BasketView from '@/views/basket/BasketView.vue';
import type { BasketResponse } from '@/generated/api-client';
import { ServerError } from '@/shared/error-handler/custom-error';
import { type ProblemDetails } from '@/shared/error-handler/custom-error';
// TODO Maia・Maris 間でProblemDetails が共通化できたら、@/generated/api-client/model から import します。
import { useNotificationStore } from '@/stores/notification/notification';
import { AxiosError, AxiosHeaders } from 'axios';
import BasketItem from '@/components/basket/BasketItem.vue';

function createBasketResponse(): BasketResponse {
  return {
    buyerId: 'xxxxxxxxxxxxxxxxxxxxxxxxxx',
    account: {
      consumptionTaxRate: 0.1,
      consumptionTax: 248,
      deliveryCharge: 500,
      totalItemsPrice: 1980,
      totalPrice: 2728,
    },
    basketItems: [
      {
        catalogItemId: 1,
        quantity: 1,
        unitPrice: 1980,
        catalogItem: {
          id: 1,
          name: 'クルーネック Tシャツ - ブラック',
          productCode: 'C000000001',
          assetCodes: [],
        },
        subTotal: 1980,
      },
      {
        catalogItemId: 2,
        quantity: 2,
        unitPrice: 4800,
        catalogItem: {
          id: 2,
          name: '裏起毛 スキニーデニム',
          productCode: 'C000000002',
          assetCodes: ['4aed07c4ed5d45a5b97f11acedfbb601'],
        },
        subTotal: 9600,
      },
    ],
  };
}

function createEmptyBasketResponse(): BasketResponse {
  return {
    buyerId: 'xxxxxxxxxxxxxxxxxxxxxxxxxx',
    account: {
      consumptionTaxRate: 0.1,
      consumptionTax: 0,
      deliveryCharge: 500,
      totalItemsPrice: 0,
      totalPrice: 0,
    },
    basketItems: [],
  };
}

function createAxiosError(problemDetails: ProblemDetails): AxiosError {
  const error = new AxiosError('', '', undefined, null, {
    status: problemDetails.status,
    statusText: '',
    headers: {},
    config: {
      headers: new AxiosHeaders({
        'Content-Type': 'application/json',
      }),
    },
    data: {
      detail: problemDetails.detail,
      exceptionId: problemDetails.exceptionId,
      exceptionValues: problemDetails.exceptionValues,
      instance: problemDetails.instance,
      status: problemDetails.status,
      title: problemDetails.title,
      type: problemDetails.type,
    },
  });
  return error;
}

function createProblemDetails({
  detail = 'No details available',
  exceptionId = 'exceptionId',
  exceptionValues = [],
  instance = '/api/',
  status = 500,
  title = 'Internal Server Error',
  type = 'about:blank',
}: Partial<ProblemDetails>): ProblemDetails {
  return {
    detail,
    exceptionId,
    exceptionValues,
    instance,
    status,
    title,
    type,
  };
}

/**
 *  vi.mock はファイルの先頭に巻き上げられるので、
 * 複数回 vi.mock を宣言すると、最後に定義されたもので上書きされてしまいます。
 * モックの振る舞いをテストケースごとに変更したい場合は、
 *  vi.hoisted を使用します。
 */
const { getBasketItemsMock } = vi.hoisted(() => {
  return {
    getBasketItemsMock: vi.fn(),
  };
});

vi.mock('@/api-client', () => ({
  basketItemsApi: {
    getBasketItems: getBasketItemsMock,
  },
}));

async function getWrapper() {
  const pinia = createTestingPinia({
    createSpy: vi.fn, // 明示的に設定する必要があります。
    stubActions: false, // 結合テストなので、アクションはモック化しないように設定します。
  });
  i18n.global.locale.value = 'ja'; // デフォルトの jsdom 環境では英語（en）に設定されるので、日本語に変更します。
  const customErrorHandler = createCustomErrorHandler();
  return mount(BasketView, {
    global: { plugins: [pinia, router, i18n, customErrorHandler] },
  });
}

describe('買い物かごのアイテムを表示する_アイテムが入っている', () => {
  let wrapper: VueWrapper;

  beforeAll(async (): Promise<void> => {
    // onMounted のタイミングで API コールを行っているので、wrapper の作成よりも先にモックする必要があります。
    getBasketItemsMock.mockResolvedValue({ data: createBasketResponse() });
    wrapper = await getWrapper();
  });

  it('取得したアイテムの情報が表示される', async () => {
    // Arrange
    const expectCount = createBasketResponse().basketItems?.length!;
    // Act
    await flushPromises();
    const basketItem = wrapper.findAllComponents(BasketItem);
    // Assert
    expect(wrapper.html()).toContain('クルーネック Tシャツ - ブラック');
    expect(wrapper.html()).toContain('裏起毛 スキニーデニム');
    expect(basketItem).toHaveLength(expectCount);
  });

  it('「買い物を続ける」ボタンが表示されている', () => {
    // Arrange
    // Act
    const button = wrapper.findAll('button')[0];
    // Assert
    expect(button.isVisible()).toBe(true);
  });

  it('「レジに進む」ボタンが表示されている', () => {
    // Arrange
    // Act
    const button = wrapper.findAll('button')[1];
    // Assert
    expect(button.isVisible()).toBe(true);
  });
});

describe('買い物かごのアイテムを表示する_アイテムが0件', () => {
  let wrapper: VueWrapper;

  beforeAll(async (): Promise<void> => {
    getBasketItemsMock.mockResolvedValue({ data: createEmptyBasketResponse() });
    wrapper = await getWrapper();
  });

  it('0件を示すメッセージが表示される', async () => {
    // Arrange
    // Act
    await flushPromises();
    // Assert
    expect(wrapper.html()).toContain('買い物かごに商品がありません。');
  });

  it('「買い物を続ける」ボタンが表示されている', () => {
    // Arrange
    // Act
    const button = wrapper.findAll('button')[0];
    // Assert
    expect(button.isVisible()).toBe(true);
  });

  it('「レジに進む」ボタンが表示されていない', () => {
    // Arrange
    // Act
    const button = wrapper.findAll('button')[1];
    // Assert
    expect(button).toBeFalsy();
  });
});

describe('買い物かごのアイテムを表示する_サーバーエラー', () => {
  it('サーバーエラー_通知ストアにサーバーエラーを示すメッセージが格納される', async () => {
    // Arrange
    const expectDetail = 'expectDetail';
    const expectExceptionId = 'serverError';
    const expectStatus = 500;
    const expectTitle = 'expectTitle';
    const problem = createProblemDetails({
      detail: expectDetail,
      exceptionId: expectExceptionId,
      status: expectStatus,
      title: expectTitle,
    });
    const error = createAxiosError(problem);
    getBasketItemsMock.mockRejectedValue(new ServerError('any message', error));
    const expectMessage = 'サーバーエラーが発生しました。';
    await getWrapper();
    const notificationStore = useNotificationStore();

    // Act
    await flushPromises();

    // Assert
    assert.equal(notificationStore.message, expectMessage);
    assert.equal(notificationStore.id, expectExceptionId);
    assert.equal(notificationStore.title, expectTitle);
    assert.equal(notificationStore.detail, expectDetail);
    assert.equal(notificationStore.status, expectStatus);
  });
});
