<template>
 <button @click="generate">点击生成摸底数据</button>
</template>

<script setup lang="ts">

import {ScreenPersonTestApi} from "@/api/tb/screenpersontest";

const generate = async () =>{
  await ScreenPersonTestApi.generateData()
}

</script>
