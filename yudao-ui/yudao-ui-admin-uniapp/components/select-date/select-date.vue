<template>
	<view>
		<uni-datetime-picker
			ref="datetime"
			:clear-icon="false"
			v-model="dateRange"
			type="daterange"
			@change="changeDate"
			style="width: 225px;"
		/>
	</view>
</template>

<script>
export default {
	name: 'select-date',
	data() {
		return {
			dateRange: []
		};
	},
	methods: {
		// 在这里执行重置日期的操作
		resetSelectDate() {
			this.dateRange = []
		},
		changeDate() {
			const result = this.dateRange.map((item, index) => {
				const time = new Date(item);
				if (index === 0) {
					time.setHours(0, 0, 0);
				} else if (index === 1) {
					time.setHours(23, 59, 59);
				}
				return `${time.getFullYear()}-${(time.getMonth() + 1).toString().padStart(2, '0')}-${time
					.getDate()
					.toString()
					.padStart(2, '0')} ${time.getHours().toString().padStart(2, '0')}:${time
					.getMinutes()
					.toString()
					.padStart(2, '0')}:${time.getSeconds().toString().padStart(2, '0')}`;
			});
			this.$emit('selectDate', result);
		}
	}
};
</script>
