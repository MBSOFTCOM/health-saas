<template>
	<view class="detail-page" v-if="detail">
		<view class="detail-header">
			<view class="detail-title">{{ detail.title }}</view>
			<view class="detail-meta">
				<text class="meta-tag" v-if="detail.categoryName">{{ detail.categoryName }}</text>
				<text class="meta-item" v-if="detail.author">作者：{{ detail.author }}</text>
				<text class="meta-item" v-if="detail.publishTime">发布：{{ detail.publishTime }}</text>
				<text class="meta-item" v-if="detail.readCount">阅读 {{ detail.readCount }}</text>
			</view>
		</view>

		<view class="detail-cover" v-if="detail.coverImage">
			<image class="cover-img" :src="detail.coverImage" mode="widthFix"></image>
		</view>

		<view class="detail-summary" v-if="detail.summary">
			<text class="summary-text">{{ detail.summary }}</text>
		</view>

		<view class="detail-content">
			<rich-text :nodes="detail.content || ''"></rich-text>
		</view>

		<view class="detail-tags" v-if="detail.tags && detail.tags.length">
			<text class="tag-label">相关标签：</text>
			<text class="tag-item" v-for="(t, i) in detail.tags" :key="i">{{ t }}</text>
		</view>

		<view class="related-section" v-if="relatedList.length">
			<view class="related-title">相关推荐</view>
			<view class="related-list">
				<view class="related-item" v-for="r in relatedList" :key="r.id" @click="toDetail(r)">
					<text class="related-text">{{ r.title }}</text>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
		</view>
	</view>
	<view v-else class="loading-tip">加载中...</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			detail: null,
			relatedList: []
		};
	},
	onLoad(e) {
		if (e.id) {
			this.loadDetail(e.id);
		}
	},
	methods: {
		async loadDetail(id) {
			try {
				const res = await ParentApi.getEducationDetail(id);
				this.detail = res.data || res;
				if (this.detail && this.detail.related) {
					this.relatedList = this.detail.related;
				}
			} catch (e) {
				uni.showToast({ title: '加载失败', icon: 'none' });
			}
		},
		toDetail(r) {
			uni.redirectTo({ url: '/pages/parent/education/detail?id=' + r.id });
		}
	}
};
</script>

<style scoped lang="scss">
.detail-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	padding: 16px;
}
.detail-header {
	background: #fff;
	border-radius: 10px;
	padding: 16px 18px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.detail-title {
		font-size: 20px;
		font-weight: 600;
		color: rgba(51, 51, 51, 1);
		line-height: 1.4;
	}
	.detail-meta {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		margin-top: 10px;
		.meta-tag {
			font-size: 11px;
			color: rgba(36, 93, 209, 1);
			background: rgba(36, 93, 209, 0.08);
			padding: 2px 8px;
			border-radius: 8px;
			margin-right: 8px;
		}
		.meta-item {
			font-size: 12px;
			color: rgba(153, 153, 153, 1);
			margin-right: 12px;
		}
	}
}
.detail-cover {
	background: #fff;
	border-radius: 10px;
	overflow: hidden;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.cover-img {
		width: 100%;
		display: block;
	}
}
.detail-summary {
	background: rgba(36, 93, 209, 0.04);
	border-left: 3px solid rgba(36, 93, 209, 1);
	padding: 12px 14px;
	margin-bottom: 16px;
	border-radius: 0 6px 6px 0;
	.summary-text {
		font-size: 13px;
		color: rgba(102, 102, 102, 1);
		line-height: 1.7;
	}
}
.detail-content {
	background: #fff;
	border-radius: 10px;
	padding: 16px 18px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	font-size: 15px;
	color: rgba(51, 51, 51, 1);
	line-height: 1.8;
}
.detail-tags {
	background: #fff;
	border-radius: 10px;
	padding: 14px 18px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.tag-label {
		font-size: 13px;
		color: rgba(102, 102, 102, 1);
		margin-right: 8px;
	}
	.tag-item {
		display: inline-block;
		font-size: 12px;
		color: rgba(36, 93, 209, 1);
		background: rgba(36, 93, 209, 0.08);
		padding: 2px 8px;
		border-radius: 10px;
		margin-right: 6px;
		margin-bottom: 4px;
	}
}
.related-section {
	background: #fff;
	border-radius: 10px;
	padding: 14px 18px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.related-title {
		font-size: 16px;
		font-weight: 600;
		color: rgba(36, 93, 209, 1);
		margin-bottom: 10px;
		padding-bottom: 8px;
		border-bottom: 1px solid rgba(238, 238, 238, 1);
	}
	.related-list {
		.related-item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 10px 0;
			border-bottom: 1px dashed rgba(238, 238, 238, 1);
			&:last-child { border-bottom: none; }
			.related-text {
				flex: 1;
				font-size: 14px;
				color: rgba(51, 51, 51, 1);
			}
		}
	}
}
.loading-tip {
	text-align: center;
	font-size: 14px;
	color: rgba(153, 153, 153, 1);
	padding: 50px 0;
}
</style>
