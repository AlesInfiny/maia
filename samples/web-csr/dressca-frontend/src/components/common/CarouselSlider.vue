<script setup lang="ts">
import { onBeforeUnmount, onMounted, reactive } from 'vue';
import {
  ChevronLeftIcon,
  ChevronRightIcon,
  MinusSmIcon,
} from '@heroicons/vue/solid';

const props = defineProps<{
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  items: any[];
}>();

const MOVE_THRESHOLD = 30;

const state = reactive({
  hasItems: true,
  currentIndex: 0,
  startX: null as number | null,
  diffX: 0,
});

const nextSlide = () => {
  state.currentIndex =
    (state.currentIndex + 1 + props.items?.length) % props.items.length;
};

const prevSlide = () => {
  state.currentIndex =
    (state.currentIndex - 1 + props.items.length) % props.items.length;
};

const selectSlide = (index: number) => {
  state.currentIndex = index;
};

const getItem = (index: number) => {
  return props.items[(index + props.items.length) % props.items.length];
};

const onTouchMove = (event: MouseEvent | TouchEvent) => {
  if (state.startX == null) {
    return;
  }
  const currentX =
    'touches' in event ? event.touches[0].clientX : event.clientX;
  state.diffX = currentX - state.startX;
};

const onTouchEnd = () => {
  if (state.startX == null) {
    return;
  }
  if (state.diffX > MOVE_THRESHOLD) {
    prevSlide();
  } else if (state.diffX < -MOVE_THRESHOLD) {
    nextSlide();
  }
  state.startX = null;
  state.diffX = 0;
};

const onTouchStart = (event: MouseEvent | TouchEvent) => {
  state.startX = 'touches' in event ? event.touches[0].clientX : event.clientX;
};

onMounted(() => {
  if (props.items.length == 0) {
    state.hasItems = false;
    return;
  }
  window.addEventListener('mousemove', onTouchMove);
  window.addEventListener('touchmove', onTouchMove);
  window.addEventListener('mouseup', onTouchEnd);
  window.addEventListener('touchend', onTouchEnd);
});

onBeforeUnmount(() => {
  window.removeEventListener('mousemove', onTouchMove);
  window.removeEventListener('touchmove', onTouchMove);
  window.removeEventListener('mouseup', onTouchEnd);
  window.removeEventListener('touchend', onTouchEnd);
});
</script>

<template>
  <template v-if="state.hasItems">
    <div data-test="body" class="container">
      <div
        class="flex justify-center items-center"
        @touchstart="onTouchStart"
        @mousedown="onTouchStart"
      >
        <ChevronLeftIcon
          class="h-10 w-10 text-gray-300 hover:text-gray-500"
          data-test="left-arrow"
          @click="prevSlide"
        />
        <template v-for="index in props.items.length">
          <template v-if="index == state.currentIndex + 1">
            <div
              :key="index"
              data-test="slider"
              :style="{
                transform: `translate3d(${state.diffX}px, 0, 0)`,
              }"
            >
              <slot v-bind="{ item: getItem(state.currentIndex) }"></slot>
            </div>
          </template>
        </template>
        <ChevronRightIcon
          class="h-10 w-10 text-gray-300 hover:text-gray-500"
          data-test="right-arrow"
          @click="nextSlide"
        />
      </div>
      <div class="flex justify-center">
        <template v-for="index in props.items.length" :key="index">
          <template v-if="index == state.currentIndex + 1">
            <MinusSmIcon class="h-10 w-10 text-gray-500"></MinusSmIcon>
          </template>
          <template v-if="index != state.currentIndex + 1">
            <MinusSmIcon
              class="h-10 w-10 text-gray-300"
              data-test="page-indicator"
              @click="selectSlide(index - 1)"
            ></MinusSmIcon>
          </template>
        </template>
      </div>
    </div>
  </template>
</template>
