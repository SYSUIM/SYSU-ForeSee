<template>
<div class="my-container"  v-if="hasTable">
  <el-card class="box-card" shadow="hover">
    <div slot="header" class="clearfix">
      <span style="color: #000; font-weight: 500;" :data="company">{{ company }}</span>
    </div>
    <div class="text item">
      <el-table
        :data="tableData"
        :show-header="false"
        :cell-style="changeCellStyle"
        border
        style="width: 100%">
        <el-table-column prop="col1" :width="130"> </el-table-column>
        <el-table-column prop="col2"> </el-table-column>
        <el-table-column prop="col3" :width="130"> </el-table-column>
        <el-table-column prop="col4"> </el-table-column>
      </el-table>
    </div>
    <!-- <div v-show="flag" class="moreTable">
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
    </div> -->
  </el-card>
</div>
</template>

<script>
// import data from '../../../public/data/知识图谱表格.json';
export default {
  name: 'Card',
  data () {
    return {
      query: this.$route.query.query,
      // query: "蓝思科技的行业类型是",
      tableData: [],
      flag: true,
      hasTable: false
    }
  },
  methods: {
    async getData () {
            let {data} = await this.$get(
                "http://121.46.19.26:8288/ForeSee/relation/" + this.query
            )
            // 本地测试
            // let  {data}  = await this.$get("/data/知识图谱表格.json");

            if(JSON.stringify(data)!="{}") {
              for(var i = 0; i < data.tableData.length; i++) {
                for (var key in data.tableData[i]) {
                    // if ((data.tableData[i])[key] == '法定代表人') {
                    if ((data.tableData[i])[key] == data.relation) {
                        // console.log(i,'行');
                        this.r = i;
                        // console.log(key.slice(3,4),'列');
                        this.c = key.slice(3,4);
                    }
                }
              }
              this.tableData = data.tableData;
              this.company = data.name;
              this.hasTable = true;
            }

            
    },
    toggle () {
        //变换 flag, 用于控制企业简介的展开与折叠
        this.flag = !this.flag
    },
    // 变换单元格的样式，回调函数的参数列表名称好像不能乱写（？）
    // 形式一
    // changeCellStyle ({rowIndex, columnIndex}) {
    //     if(rowIndex == 3 && (columnIndex == 2 || columnIndex == 3))
    //         return 'color: #000; font-weight: 600';
    //     // if(columnIndex == 0 || columnIndex == 2 )
    //     //     return 'color: #909399; font-weight: 600';
    // },
    // 形式二
    // changeCellStyle ({row, column, columnIndex}) {
    //     if (columnIndex === 0 && row[column.property].indexOf('登记') > -1) {
    //         return 'color: red'
    //     }
    // },
    // 形式三
    changeCellStyle ({rowIndex, columnIndex}) {
        if(rowIndex == this.r && (columnIndex == this.c || columnIndex == (this.c-1)))
            return 'color: #000; font-weight: 600';
    },
  },
  mounted () {
    this.getData();
  }
}
</script>

<style scoped>
  .my-container {
    margin-left: 63px;
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
  .box-card {
    width: 100%;
    margin-top: 80px;
  }
</style>
