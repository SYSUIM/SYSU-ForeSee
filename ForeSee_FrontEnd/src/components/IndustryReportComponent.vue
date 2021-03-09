<template>
    <div>
      <div class="report">
        <!-- <el-button class="clearBtn" @click="resetDateFilter">清除日期过滤器</el-button> -->
        <!-- <el-button class="clearBtn" @click="clearFilter">清除过滤器</el-button> -->
        <el-table
        ref="filterTable"
        :data="tableData"
        style="width: 100%">
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="摘要">
                    <span>{{ props.row.abstract }}</span>
                  </el-form-item>
                  <!-- item 2 -->
                  <!-- <el-form-item label="所属店铺">
                  <span>{{ props.row.shop }}</span>
                  </el-form-item> -->
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
            prop="pub_date"
            sortable
            label="发表日期"
            width="180"
            column-key="pub_date"
            >
            <template slot-scope="scope">              
                {{scope.row.pub_date}}
            </template>
            </el-table-column>
            <el-table-column
            prop="title"
            label="标题"
            :formatter="formatter">
            <template slot-scope="scope">
              <a :href="scope.row.link" target="_blank">{{scope.row.title}}</a>
            </template>
            </el-table-column>
            <el-table-column
            prop="analysis_year"
            label="分析年份"
            width="100"
            :filters="filters_ay"
            :filter-method="filterTag"
            filter-placement="bottom-end">
            <template slot-scope="scope">
                <el-tag
                :type="scope.row.analysis_year === '2020' ? 'success' : 'primary'"
                disable-transitions>{{scope.row.analysis_year}}</el-tag>
            </template>
            </el-table-column>
        </el-table>
      </div>

      <!-- 分页组件 -->
      <div class="block">
          <el-pagination
          :page-size="20"
          @current-change="handleCurrentChange"
          layout="prev, pager, next"
          :total="totalRecords">
          </el-pagination>
      </div>
    </div>
</template>

<script>
  export default {
    data() {
      return {
        industryCode: decodeURI(this.$route.query.industryCode),
        totalRecords: 11, //总记录数totalRecords = 82 时，默认每页展示 10 条，因此绑定后总页数为 9
        currentPage: 1,
        tableData: [],
        filters_ay: [
          {text:'2016',value:'2016'},
          {text:'2017',value:'2017'},
          {text:'2018',value:'2018'},
          {text:'2019',value:'2019'},
          {text:'2020',value:'2020'},
        ]
      }
    },
    methods: {
      async load(val) {
        // let { data } = await this.$get("/data/Internet_article_info.json");
        let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/report/"+ this.industryCode + "/" + val);
        this.tableData = data.information;
        this.totalRecords = data.totalRecords;
        // console.log(data)
        
      },
      // resetDateFilter() {
      //   this.$refs.filterTable.clearFilter('date');
      // },
      // clearFilter() {
      //   this.$refs.filterTable.clearFilter();
      // },
      //formatter(row, column) {
      formatter(row) {
        return row.title;
      },
      filterTag(value, row) {
        return row.analysis_year === value;
      },
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      },
      // 分页组件
      handleCurrentChange(val) {
          console.log(`当前页: ${val}`);
          this.currentPage = val;
          this.load(val);
      }
    },
    created() {
      this.load(1);
      // this.show();
    }
  }
</script>

<style scoped>
  .report {
    width: 70%;
    margin: 20px auto;
  }
  .report .clearBtn:hover {
    background-color: rgb(255,218,8);
    color: white;
    font-weight: 700;
    border: 1px solid white;
  }
  .el-table th>.cell.highlight {
    color: #909399;
  }
  .el-table .ascending .sort-caret.ascending {
    border-top-color: #FFD808;
  }
  .el-table .descending .sort-caret.descending {
    border-top-color: #FFD808;
  }
  
  /* 分页组件 */
  .block {
    margin-top: 70px;
  }
  div.el-pagination {
    text-align: center;
  }
</style>

<style>
  div.el-table__expand-icon {
    background-color: #F4F4F4 !important;
    border-radius: 3px;
  }
</style>