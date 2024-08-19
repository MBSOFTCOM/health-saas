<template>
  <view>
    <view>
      <uni-data-select
          v-model="selectTable"
          :localdata="tableList"
          @change="getFields"/>
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
        pageSize:2,
        pageNo:1
      },
      tableList:[],
      fieldList:[],
      selectTable:'',
    };
  },
  methods:{
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
      console.log(this.data)
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