<template>
  <el-card class="box-card" shadow="hover">
    <div slot="header" class="clearfix">
      <span>基本信息</span>
    </div>
    <div class="text item">
      <el-table
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column
          prop="name"
          :label="label1">
        </el-table-column>
        <el-table-column
          prop="value"
          :label="label2"
          width="450">
        </el-table-column>
      </el-table>
    </div>
    <div v-show="flag" class="moreTable">
      <a href="javascript:void(0)" @click="toggle" class="toggle">∨ 显示更多</a>
    </div>
    <div v-show="!flag" >
      <div class="text item">
      <el-table
        :data="tableData2"
        border
        style="width: 100%">
        <el-table-column
          prop="name"
          :label="label3">
        </el-table-column>
        <el-table-column
          prop="value"
          :label="label4"
          width="450">
        </el-table-column>
      </el-table>
      </div>
      <div class="moreTable">
      <a href="javascript:void(0)" @click="toggle" class="toggle">∧ 收起</a>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'Card',
  data () {
    return {
      stockCode: this.$route.query.stockCode,
      label1: "",
      label2: "",
      label3: "",
      label4: "",
      tableData: [],
      tableData2: [],
      flag: true
    }
  },
  methods: {
    async getData () {
            let {data} = await this.$get(
                // "http://121.46.19.26:8288/ForeSee/allInfo/" + this.stockCode
                "http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode
            )
            // 本地测试
            // let  {data}  = await this.$get("/data/detail.json");

            // 表格 1
            this.label1 = data.tableData[1].name;
            this.label2 = data.tableData[1].value;
            this.tableData.push(data.tableData[0]);
            for (var i = 2; i <= 6; i++)
                this.tableData.push(data.tableData[i]);
            
            // 表格 2
            this.label3 = data.tableData2[0].name;
            this.label4 = data.tableData2[0].value;
            for (var j = 1; j < data.tableData2.length; j++)
                this.tableData2.push(data.tableData2[j]);
    },
    toggle () {
        //变换 flag, 用于控制企业简介的展开与折叠
        this.flag = !this.flag
    }
  },
  mounted () {
    this.getData();
  }
}
</script>

<style>
  th {
    font-weight: normal;
    color: #606266;
  } 
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
  div.el-card__header {
    color: #FFD808;
  }
  .box-card {
    width: 100%;
    margin-top: 30px;
  }
</style>
<style scoped>
  .moreTable {
    text-align: center;
    font-size: 10px;
    color: #606266;
  }
</style>
