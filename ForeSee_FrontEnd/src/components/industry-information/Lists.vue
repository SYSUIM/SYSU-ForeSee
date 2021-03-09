<template>
  <el-table
    :data="tableData"
    style="width: 100%">
    <el-table-column type="expand">
      <template slot-scope="props">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item label="摘要">
            <span>{{ props.row.desc }}</span>
          </el-form-item>
          <!-- item 2 -->
          <!-- <el-form-item label="所属店铺">
            <span>{{ props.row.shop }}</span>
          </el-form-item> -->
        </el-form>
      </template>
    </el-table-column>
    <el-table-column
      label="序号"
      type="index"
      width="150">
    </el-table-column>
    <el-table-column
      label="发表时间"
      prop="time"
      sortable>
    </el-table-column>
    <el-table-column
      label="标题"
      prop="title">
    </el-table-column>
    <el-table-column
      label="分析年份"
      prop="analysis_year"
      sortable>
    </el-table-column>
  </el-table>
</template>

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>

<script>
  export default {
    data() {
      return {
        tableData: [{
          time: '2020-10-15',
          name: '好滋好味鸡蛋仔',
          title: '江浙小吃、小吃零食',
          desc: '荷兰优质淡奶，奶香浓而不腻',
          analysis_year: '2020'
        },{
          time: '2020-10-15',
          name: '好滋好味鸡蛋仔',
          title: '江浙小吃、小吃零食',
          desc: '荷兰优质淡奶，奶香浓而不腻',
          analysis_year: '2019'
        },{
          time: '2020-10-14',
          name: '好滋好味鸡蛋仔',
          title: '江浙小吃、小吃零食',
          desc: '荷兰优质淡奶，奶香浓而不腻',
          analysis_year: '2020'
        },{
          time: '2020-10-13',
          name: '好滋好味鸡蛋仔',
          title: '江浙小吃、小吃零食',
          desc: '荷兰优质淡奶，奶香浓而不腻',
          analysis_year: '2018'
        },{
          time: '2020-10-13',
          name: '好滋好味鸡蛋仔',
          title: '江浙小吃、小吃零食',
          desc: '荷兰优质淡奶，奶香浓而不腻',
          analysis_year: '2019'
        }]
      }
    }
  }
</script>