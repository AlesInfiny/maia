import { nextTick } from 'vue'
import { describe, it, expect } from 'vitest'
import { shallowMount } from '@vue/test-utils'
import CarouselSlider from '@/components/common/CarouselSlider.vue'

const items = [
  { name: 'test1', id: 1 },
  { name: 'test2', id: 2 },
  { name: 'test3', id: 3 },
]

describe('CarouselSlider', () => {
  it('slotにコンテンツが挿入できる', () => {
    const wrapper = shallowMount(CarouselSlider, {
      props: { items },
      slots: {
        default: '<div data-test="slotContent">slot content</div>',
      },
      stubs: {
        ChevronRightIcon: true,
      },
    })

    const slotContent = wrapper.find('[data-test="slotContent"]')

    expect(slotContent.exists()).toBe(true)
    expect(slotContent.text()).toBe('slot content')
  })

  it('引数の配列が表示できる', () => {
    const wrapper = shallowMount(CarouselSlider, {
      props: { items },
      slots: {
        default:
          '<template #default="{ item }" ><div data-test="slotContent">{{ item.name }}</div></template>',
      },
    })

    const slotContent = wrapper.find('[data-test="slotContent"]')

    expect(slotContent.exists()).toBe(true)
    expect(slotContent.text()).toBe('test1')
  })

  it('引数の要素が0の時なにも表示しない', async () => {
    const wrapper = shallowMount(CarouselSlider, {
      props: { items: [] },
      slots: {
        default:
          '<template #default="{ item }" ><div data-test="slotContent">{{ item.name }}</div></template>',
      },
    })

    await nextTick()

    const content = wrapper.find('[data-test="body"]')

    expect(content.exists()).toBe(false)
  })

  it('右矢印をクリックすると表示要素が進む', async () => {
    const wrapper = shallowMount(CarouselSlider, {
      props: { items },
      slots: {
        default:
          '<template #default="{ item }" ><div data-test="slotContent">{{ item.name }}</div></template>',
      },
    })

    await wrapper.find('[data-test="right-arrow"]').trigger('click')

    const slotContent = wrapper.find('[data-test="slotContent"]')

    expect(slotContent.exists()).toBe(true)
    expect(slotContent.text()).toBe('test2')
  })

  it('左矢印をクリックすると表示要素が戻る', async () => {
    const wrapper = shallowMount(CarouselSlider, {
      props: { items },
      slots: {
        default:
          '<template #default="{ item }" ><div data-test="slotContent">{{ item.name }}</div></template>',
      },
    })

    await wrapper.find('[data-test="left-arrow"]').trigger('click')

    const slotContent = wrapper.find('[data-test="slotContent"]')

    expect(slotContent.exists()).toBe(true)
    expect(slotContent.text()).toBe('test3')
  })

  it('表示要素を左にスライドすると要素が進む', async () => {
    const wrapper = shallowMount(CarouselSlider, {
      attachTo: document.body,
      props: { items },
      slots: {
        default:
          '<template #default="{ item }" ><div data-test="slotContent">{{ item.name }}</div></template>',
      },
    })

    const slider = wrapper.find('[data-test="slider"')

    const mouseDown = new MouseEvent('mousedown', { bubbles: true })
    const mouseMove = new MouseEvent('mousemove', {
      bubbles: true,
      clientX: -100,
    })
    const mouseUp = new MouseEvent('mouseup', { bubbles: true, clientX: -100 })

    slider.element.dispatchEvent(mouseDown)
    slider.element.dispatchEvent(mouseMove)
    slider.element.dispatchEvent(mouseUp)

    await nextTick()

    const slotContent = wrapper.find('[data-test="slotContent"]')

    expect(slotContent.exists()).toBe(true)
    expect(slotContent.text()).toBe('test2')
  })

  it('表示要素を右にスライドすると要素が進む', async () => {
    const wrapper = shallowMount(CarouselSlider, {
      attachTo: document.body,
      props: { items },
      slots: {
        default:
          '<template #default="{ item }" ><div data-test="slotContent">{{ item.name }}</div></template>',
      },
    })

    const slider = wrapper.find('[data-test="slider"')

    const mouseDown = new MouseEvent('mousedown', { bubbles: true })
    const mouseMove = new MouseEvent('mousemove', {
      bubbles: true,
      clientX: 100,
    })
    const mouseUp = new MouseEvent('mouseup', { bubbles: true, clientX: 100 })

    slider.element.dispatchEvent(mouseDown)
    slider.element.dispatchEvent(mouseMove)
    slider.element.dispatchEvent(mouseUp)

    await nextTick()

    const slotContent = wrapper.find('[data-test="slotContent"]')

    expect(slotContent.exists()).toBe(true)
    expect(slotContent.text()).toBe('test3')
  })

  it('ページインジケーターをクリックするとそれが示す要素を表示する', async () => {
    const wrapper = shallowMount(CarouselSlider, {
      props: { items },
      slots: {
        default:
          '<template #default="{ item }" ><div data-test="slotContent">{{ item.name }}</div></template>',
      },
    })

    const indicator = wrapper.findAll('[data-test="page-indicator"')
    await indicator[1].trigger('click')

    const slotContent = wrapper.find('[data-test="slotContent"]')

    expect(slotContent.exists()).toBe(true)
    expect(slotContent.text()).toBe('test3')
  })
})
