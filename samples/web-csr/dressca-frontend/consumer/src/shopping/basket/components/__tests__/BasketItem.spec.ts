import { describe, it, expect, beforeEach } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import { i18n } from '@/system-common/locales/i18n'
import type { BasketLine } from '../../models/basket-line'
import BasketItem from '../BasketItem.vue'

/**
 * 買い物かごの行を生成します。
 * 主にテストやモックデータとして利用することを想定しています。
 * @returns `BasketLine` 型のオブジェクト
 */
function createBasketLine(): BasketLine {
  return {
    displayItemId: '019b76da-a800-7004-8001-00000000000a',
    name: '',
    assetCodes: [],
    quantity: 2,
    subTotal: 100000,
    unitPrice: 50000,
    available: true,
  }
}

describe('BasketItem', () => {
  beforeEach(() => {
    i18n.global.locale.value = 'ja'
  })

  it('小計が日本円形式で表示できる', () => {
    // Arrange
    const basketLine = createBasketLine()
    const available = true

    // Act
    const wrapper = mount(BasketItem, {
      props: { item: basketLine, available },
      global: { plugins: [i18n] },
    })

    // Assert
    expect(wrapper.text()).toContain('￥100,000')
  })

  it('単価が日本円形式で表示できる', () => {
    // Arrange
    const basketLine = createBasketLine()
    const available = true

    // Act
    const wrapper = mount(BasketItem, {
      props: { item: basketLine, available },
      global: { plugins: [i18n] },
    })

    // Assert
    expect(wrapper.text()).toContain('￥50,000')
  })

  it('販売中止中のメッセージが表示できる', () => {
    // Arrange
    const basketLine = createBasketLine()
    const available = false

    // Act
    const wrapper = mount(BasketItem, {
      props: { item: basketLine, available },
      global: { plugins: [i18n] },
    })

    // Assert
    expect(wrapper.text()).toContain(
      'こちらの商品は現在販売しておりません。買い物かごから削除してください。',
    )
  })

  it('数量が 0 のとき更新ボタンが非活性になる', async () => {
    const wrapper = mount(BasketItem, {
      props: { item: createBasketLine(), available: true },
      global: { plugins: [i18n] },
    })

    const quantityInput = wrapper.find('input[type="number"]')
    await quantityInput.setValue('0')
    await quantityInput.trigger('blur')
    await flushPromises()

    expect(wrapper.find('button').attributes('disabled')).toBeDefined()
  })

  it('数量が 1000 のとき更新ボタンが非活性になる', async () => {
    const wrapper = mount(BasketItem, {
      props: { item: createBasketLine(), available: true },
      global: { plugins: [i18n] },
    })

    const quantityInput = wrapper.find('input[type="number"]')
    await quantityInput.setValue('1000')
    await quantityInput.trigger('blur')
    await flushPromises()

    expect(wrapper.find('button').attributes('disabled')).toBeDefined()
  })

  it('数量が有効な値に変更されたとき更新ボタンが活性になる', async () => {
    const wrapper = mount(BasketItem, {
      props: { item: createBasketLine(), available: true },
      global: { plugins: [i18n] },
    })

    const quantityInput = wrapper.find('input[type="number"]')
    await quantityInput.setValue('3')
    await quantityInput.trigger('blur')
    await flushPromises()

    expect(wrapper.find('button').attributes('disabled')).toBeUndefined()
  })
})
