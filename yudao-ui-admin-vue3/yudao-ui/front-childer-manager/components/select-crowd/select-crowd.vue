<template>
	<w-select
		v-model="chooseValue"
		:list="list"
		multiple
		valueName="content"
		keyName="id"
		@change="change"
		width="210px"
		height="40px"
		defaultValue="请选择人群分类"
		showClose
	></w-select>
</template>
<script>
export default {
	name: 'select-crowd',
	props: ['isVisible'], 
	data() {
		return {
			chooseValue: [],
			list: [
				{
					content: '重点人群',
					id: '11'
				},
				{
					content: '非重点人群',
					id: '12'
				},
				{
					content: '学生',
					id: '1'
				},
				{
					content: '老年人',
					id: '2'
				},
				{
					content: '教职工',
					id: '4'
				},
				{
					content: '密接者',
					id: '8'
				},
				{
					content: '糖尿病',
					id: '16'
				},
				{
					content: '僧尼',
					id: '32'
				},
				{
					content: '既往患者',
					id: '64'
				}
			]
		};
	},
	watch: {
		isVisible(){
			  this.chooseValue = []; // 如果 newVal 为 true，清空 chooseValue
		}
	},
	methods: {
		change(e) {
			const firstVal = [];
			const secondVal = [];
			if (Array.isArray(this.chooseValue) && this.chooseValue.length > 0) {
				this.chooseValue.forEach((item) => {
					const id = item.id;
					if (id === '11' || id === '12') {
						firstVal.push(parseInt(id[1]));
					} else {
						secondVal.push(parseInt(id));
					}
				});
			}
			this.$emit('updateValues', { firstVal, secondVal });
		}
	}
};
</script>

<style lang="scss"></style>
