<template>
	<view class="edu-page">
		<!-- 推荐位：结合受检者阳性结果推荐 -->
		<view class="recommend-section" v-if="recommendList.length">
			<view class="section-title">
				<text class="title-text">为您推荐</text>
				<text class="title-desc">根据阳性结果推荐</text>
			</view>
			<scroll-view scroll-x class="recommend-scroll">
				<view class="recommend-item" v-for="r in recommendList" :key="r.id" @click="toDetail(r)">
					<view class="recommend-card">
						<image v-if="r.coverImage" class="recommend-img" :src="r.coverImage" mode="aspectFill"></image>
						<view v-else class="recommend-img recommend-img-placeholder">
							<text class="placeholder-text">{{ r.title ? r.title.charAt(0) : '健' }}</text>
						</view>
						<view class="recommend-title">{{ r.title }}</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 分类筛选 -->
		<scroll-view scroll-x class="category-bar">
			<view
				class="category-item"
				:class="{ active: currentCategory === '' }"
				@click="switchCategory('')"
			>
				全部
			</view>
			<view
				v-for="c in categoryList"
				:key="c.code"
				class="category-item"
				:class="{ active: currentCategory === c.code }"
				@click="switchCategory(c.code)"
			>
				{{ c.name }}
			</view>
		</scroll-view>

		<!-- 宣教列表 -->
		<view class="edu-list" v-if="eduList.length">
			<view class="edu-card" v-for="e in eduList" :key="e.id" @click="toDetail(e)">
				<view class="card-left">
					<image v-if="e.coverImage" class="card-img" :src="e.coverImage" mode="aspectFill"></image>
					<view v-else class="card-img card-img-placeholder">
						<text class="placeholder-text">{{ e.title ? e.title.charAt(0) : '健' }}</text>
					</view>
				</view>
				<view class="card-right">
					<view class="card-title">{{ e.title }}</view>
					<view class="card-summary">{{ e.summary || e.description || '' }}</view>
					<view class="card-meta">
						<text class="meta-tag" v-if="e.categoryName">{{ e.categoryName }}</text>
						<text class="meta-item" v-if="e.readCount">阅读 {{ e.readCount }}</text>
					</view>
				</view>
			</view>
		</view>
		<view v-else class="empty-state">
			<text class="empty-text">暂无宣教内容</text>
		</view>
	</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			currentPatient: null,
			categoryList: [],
			currentCategory: '',
			recommendList: [],
			eduList: []
		};
	},
	onLoad() {
		const cache = uni.getStorageSync('parent_current_patient');
		if (cache) this.currentPatient = cache;
		this.loadCategory();
		this.loadList();
		this.loadRecommend();
	},
	methods: {
		async loadCategory() {
			try {
				const res = await ParentApi.getEducationCategoryList();
				this.categoryList = res.list || res.data || res || [];
			} catch (e) {
				this.categoryList = [];
			}
		},
		async loadList() {
			try {
				const params = this.currentCategory ? { category: this.currentCategory } : {};
				const res = await ParentApi.getEducationList(params);
				this.eduList = res.list || res.data || res || [];
			} catch (e) {
				this.eduList = [];
			}
		},
		async loadRecommend() {
			if (!this.currentPatient) return;
			try {
				const res = await ParentApi.getRecommendedEducation(this.currentPatient.id);
				this.recommendList = res.list || res.data || res || [];
			} catch (e) {
				this.recommendList = [];
			}
		},
		switchCategory(code) {
			this.currentCategory = code;
			this.loadList();
		},
		toDetail(e) {
			uni.navigateTo({ url: '/pages/parent/education/detail?id=' + e.id });
		}
	}
};
</script>

<style scoped lang="scss">
.edu-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	padding: 16px;
}
.recommend-section {
	background: #fff;
	border-radius: 10px;
	padding: 14px 16px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.section-title {
		display: flex;
		align-items: baseline;
		margin-bottom: 10px;
		.title-text { font-size: 16px; font-weight: 600; color: rgba(36, 93, 209, 1); }
		.title-desc { font-size: 12px; color: rgba(153, 153, 153, 1); margin-left: 8px; }
	}
	.recommend-scroll {
		white-space: nowrap;
	}
	.recommend-item {
		display: inline-block;
		margin-right: 10px;
	}
	.recommend-card {
		width: 140px;
		.recommend-img {
			width: 140px;
			height: 90px;
			border-radius: 6px;
			background: rgba(238, 238, 238, 1);
		}
		.recommend-img-placeholder {
			display: flex;
			align-items: center;
			justify-content: center;
			background: linear-gradient(135deg, rgba(36, 93, 209, 0.4), rgba(80, 104, 242, 0.4));
			.placeholder-text {
				font-size: 28px;
				font-weight: 600;
				color: rgba(255, 255, 255, 0.9);
			}
		}
		.recommend-title {
			font-size: 13px;
			color: rgba(51, 51, 51, 1);
			margin-top: 6px;
			line-height: 1.4;
			white-space: normal;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			overflow: hidden;
		}
	}
}
.category-bar {
	white-space: nowrap;
	margin-bottom: 12px;
	.category-item {
		display: inline-block;
		padding: 6px 14px;
		margin-right: 8px;
		font-size: 13px;
		color: rgba(102, 102, 102, 1);
		background: #fff;
		border-radius: 16px;
		&.active {
			background: rgba(36, 93, 209, 1);
			color: #fff;
		}
	}
}
.edu-list {
	.edu-card {
		display: flex;
		background: #fff;
		border-radius: 10px;
		padding: 12px;
		margin-bottom: 12px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.card-left {
			margin-right: 12px;
		}
		.card-img {
			width: 110px;
			height: 80px;
			border-radius: 6px;
			background: rgba(238, 238, 238, 1);
		}
		.card-img-placeholder {
			display: flex;
			align-items: center;
			justify-content: center;
			background: linear-gradient(135deg, rgba(36, 93, 209, 0.4), rgba(80, 104, 242, 0.4));
			.placeholder-text {
				font-size: 24px;
				font-weight: 600;
				color: rgba(255, 255, 255, 0.9);
			}
		}
		.card-right {
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			.card-title {
				font-size: 15px;
				font-weight: 600;
				color: rgba(51, 51, 51, 1);
				line-height: 1.4;
			}
			.card-summary {
				font-size: 12px;
				color: rgba(102, 102, 102, 1);
				margin-top: 4px;
				line-height: 1.5;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				-webkit-box-orient: vertical;
				overflow: hidden;
			}
			.card-meta {
				margin-top: 6px;
				.meta-tag {
					font-size: 11px;
					color: rgba(36, 93, 209, 1);
					background: rgba(36, 93, 209, 0.08);
					padding: 1px 6px;
					border-radius: 8px;
					margin-right: 8px;
				}
				.meta-item {
					font-size: 11px;
					color: rgba(153, 153, 153, 1);
				}
			}
		}
	}
}
.empty-state {
	background: #fff;
	border-radius: 10px;
	padding: 50px 20px;
	text-align: center;
	.empty-text { font-size: 14px; color: rgba(153, 153, 153, 1); }
}
</style>
