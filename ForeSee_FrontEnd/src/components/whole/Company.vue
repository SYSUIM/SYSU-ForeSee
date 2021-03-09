<template>
    <!-- content -->
    <div class="section" v-loading="loading">
			<div class="container">
				<div class="row" >
					<!-- 第一个模块：拿到数据后可修改成循环插入 -->
					<div class="col-sm-4 col-md-4" v-for="item in content" :key="item.stock_code">
						<div class="box-image-1">
							<router-link :to="'/detail'+'?stockCode='+item.stock_code"> 
								<div class="media-img">
									<img :src="item.logo" alt="" class="img-fluid">
								</div>
								<div class="body-content">
									<h4 class="title">{{item.former_name}}</h4>
								</div>
							</router-link>
						</div>
					</div>
				</div>
				<div v-if="hasMore">

				<!-- 分页组件 -->
				<div class="block">
					<el-pagination
					:page-size="6"
					@current-change="handleCurrentChange"
					layout="prev, pager, next"
					:total="sumRecords">
					</el-pagination>
				</div>

				</div>

			</div>
    </div>
</template>

<script src="../../public/js/vendor/modernizr.min.js"></script>
<script>
export default {
	// props:["content"],
	data(){
		return{
			query: decodeURI(this.$route.query.query),
            // content: [
			// 	{"former_name":"新希望六和股份有限公司","stock_code":"000876","logo":"https://zhengxin-pub.cdn.bcebos.com/logopic/c528e895030e26c8b611c7ce2b3001db_fullsize.jpg"},{"former_name":"新希望乳业股份有限公司","stock_code":"002946","logo":"https://zhengxin-pub.cdn.bcebos.com/logopic/aa494c5a0d224e7504cae9b5a555b147_fullsize.jpg"},{"former_name":"中国南方航空股份有限公司","stock_code":"600029","logo":"https://zhengxin-pub.cdn.bcebos.com/logopic/36128aebc475028bf7ff86a1e27eae0e_fullsize.jpg"},{"former_name":"中国东方航空股份有限公司","stock_code":"600115","logo":"https://zhengxin-pub.cdn.bcebos.com/logopic/91d4cb829baee1536280ebb91bc18bee_fullsize.jpg"},{"former_name":"中信海洋直升机股份有限公司","stock_code":"000099","logo":"https://zhengxin-pub.cdn.bcebos.com/logopic/dce024d37140b4774dab0590e9d03de2_fullsize.jpg"},{"former_name":"海南航空控股股份有限公司","stock_code":"900945","logo":"https://zhengxin-pub.cdn.bcebos.com/logopic/6ec0c2bbda9b285ebce45348f812a88e_fullsize.jpg"}
			// ],
			content: [],
			hasMore: false,
			sumRecords: 0,
			loading: true, // 是否正在加载，与根元素绑定，v-loading="loading"
			page: 1
		};
	},

	methods: {
		async getData () {
            let {data} = await this.$get(
                "http://121.46.19.26:8288/ForeSee/companyQuery/" + this.query + "/" + this.page
            )
			// console.log('企业检索>',data)
		
			// 数据处理
			this.content = data.company;
			let n = data.totalRecords;
			if(n > 6) {
				this.hasMore = true;
			}
			this.sumRecords = n;
			this.loading = false;
		},
		async getContent (val) {
            this.loading = true;
            let { data } = await this.$get(
				"http://121.46.19.26:8288/ForeSee/companyQuery/" + this.query + "/" + val
			);
            this.content = data.company;
            this.loading = false;
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.currentPage = val;
            this.getContent(val);
        },
	},
	created(){
		this.getData();
		// console.log(this.content)
	},
	watch: {
        sumRecords () {
            this.$emit('listenToChildren', this.sumRecords); // 将数据从Company组件传给上级<AnchorList>组件
        }
    }
}
</script>

<style scoped>
	.col-sm-4 {
		width: 20%;
		height: 220px;
	}
	.box-image-1 {
		/* border: 1px solid black; */
		text-align: center;
		vertical-align: middle;
		height: 200px;
	}
	.box-image-1 a {
		margin: 10px auto;
	}
	.box-image-1 .media-img{
		margin: 10px;
		height: 80px;
	}
	.box-image-1 .media-img img {
		height: 80px;
	}
	.box-image-1:hover {
		transform: scale(1.05,1.05);
		box-shadow: 7px 7px 7px rgba(0,0,0,.3);
		transition: all .2s;
	}
	.block {
        margin-top: 20px;
    }
	div.el-pagination {
        text-align: center;
    }
	
</style>