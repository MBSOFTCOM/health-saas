<template>
  <view>
    <view>
      <uni-data-select
          v-model="selectTable"
          :localdata="tableList"
          @change="getFields"/>
    </view>
	<view>
		<up-form
			labelWidth='100'
			labelPosition="left"
			:model="formData"
			:rules="rules"
			ref="form"
		>
			<up-form-item label="次序字段名" borderBottom prop="field">
				<up-input placeholder='次序字段名' border="surround" v-model="formData.field"/>
			</up-form-item>
			<up-form-item label="次序" borderBottom prop="order">
				<up-input placeholder="请输入次序" border="surround" v-model="formData.order"/>
			</up-form-item>
			<up-form-item label="身份证" borderBottom prop="idNum">
				<up-input placeholder="请输入身份证" border="surround" v-model="formData.idNum"/>
			</up-form-item>
			<up-form-item label="数据状态" borderBottom prop="statusFlag">
				<up-input placeholder="请输入数据状态" border="surround" v-model="formData.statusFlag"/>
			</up-form-item>
			<up-button type="primary" text="确定" @click="updateStatusFlag"/>
		</up-form>
	</view>
    <uni-table :style="tableStyle" ref="table" stripe emptyText="暂无更多数据">
      <uni-tr>
        <uni-th v-for="field in fieldList">{{field.name}}</uni-th>
      </uni-tr>
      <uni-tr v-for="item in data">
        <uni-td v-for="key in objKey">{{item[key]}}</uni-td>
      </uni-tr>

    </uni-table>
    <view class="uni-pagination-box">
      <uni-pagination
          show-icon
          :page-size="pageParams.pageSize"
          :current="pageParams.pageNo"
          :total="total"
          @change="changePage"
      />
    </view>
  </view>
</template>

<script>
import * as SQLiteManager from "@/api/SQLiteManage/SQLiteManage";
import {countPage, getPage} from "../../../api/SQLiteManage/SQLiteManage";

export default {
  name: "index",
  computed: {
    tableStyle() {
      return {
        overflow: 'auto'
      };
    }
  },
  data() {
    return {
      data:[],
      objKey:[],
      total:0,
      pageParams:{
        pageSize:10,
        pageNo:1
      },
      tableList:[],
      fieldList:[],
      selectTable:'',
	  formData:{
		  order:'',
		  field:'',
		  statusFlag:null,
		  idNUm:null
	  }
    };
  },
  methods:{
	updateStatusFlag(){
		SQLiteManager.setStatusFlag(this.selectTable,this.formData.idNum,this.fieldList.order,this.formData.field,this.formData.statusFlag)
	},
    setTableListFormatter(list){
      for (let i = 0; i < list.length; i++) {
        let item={
          value:list[i].name,
          text:list[i].name
        }
        this.tableList.push(item)
      }
    },
    async getFields(){
      let data=await SQLiteManager.getFields(this.selectTable)
      if (data && data.length>=1){
        this.fieldList=data
      }
      await this.getTableData(this.selectTable)
    },
    async getTableData(table){
      this.data=await SQLiteManager.getPage(table,this.pageParams)
      let count=await SQLiteManager.countPage(table,this.pageParams)
      this.total=count[0].num
      // console.log(this.data)
      if (this.data && this.data.length>0){
        this.objKey=Object.keys(this.data[0])
      }
    },
    changePage(e){
      this.pageParams.pageNo = e.current;
      this.getTableData(this.selectTable)
    },
    async getTables(){
      this.tableList=[]
      let data=await SQLiteManager.getTableName()
      this.setTableListFormatter(data)
    }
  },
   onShow(){
   this.getTables()

  }
}
</script>

<style scoped>

</style>