<template>
	<view style="display: flex; justify-content: space-between; flex-direction: column">
		<uni-data-select
			style="width: 20vw; background-color: #fff; margin-bottom: 10px"
			v-model="val"
			placeholder="请选择人群分类"
			:localdata="items1"
			@change="change"
		></uni-data-select>
		<uni-data-checkbox
			v-if="this.val == 1"
			multiple
			v-model="checkbox"
			@change="onchange"
			:localdata="items2"
		></uni-data-checkbox>
	</view>
</template>

<script>
export default {
	name: 'population-classify',
	data() {
		return {
			val: '',
			items1: [
				{
					text: '重点人群',
					value: '1'
				},
				{
					text: '非重点人群',
					value: '2'
				},
				{
					text: '教职工',
					value: '4'
				}
			],
			checkbox: [],
			items2: [
				{
					text: '学生',
					value: '1'
				},
				{
					text: '老年人',
					value: '2'
				},
				{
					text: '教职工',
					value: '4'
				},
				{
					text: '密接者',
					value: '8'
				},
				{
					text: '糖尿病',
					value: '16'
				},
				{
					text: '僧尼',
					value: '32'
				},
				{
					text: '既往患者',
					value: '64'
				}
			]
		};
	},
	methods: {
		change(e) {
			this.$emit('bindVal', this.val);
		},
		onchange(e) {
			this.$emit('bindVal', this.checkbox);
		}
	}
};
</script>

<style lang="scss"></style>
