<template>
<!-- content -->
<div class="section">
		<div class="content-wrap">
			<div class="container">

				<!-- 六个图形模块 -->
				<div class="row" >
					<!-- 第一个模块：拿到数据后可修改成循环插入 -->
					<div class="col-sm-4 col-md-4" v-for="item in content" :key="item.stock_code">
						<div class="box-image-1">
							<!-- <a href="project-detail.html" title="Industrial Complex"> -->
							<router-link :to="'/detail'+'?stockCode='+item.stock_code"> 
								<div class="media-img">
									<img :src="item.logo" alt="" class="img-fluid">
								</div>
								<div class="body-content">
									<h4 class="title">{{item.former_name}}</h4>
									<!-- <p class="category">{{item.registered_address}}</p> -->
									<p class="category"><span>股票代码: </span>{{item.stock_code}}</p>
								</div>
							</router-link>
						</div>
					</div>
				</div>

				
			</div>
		</div>
</div>
</template>

<script src="../../public/js/vendor/modernizr.min.js"></script>
<script>
export default {
	props:["content"],
	data(){
		return{
			
		};
	},
	created(){
		// console.log(this.content)
	}
}
</script>

<style src="../../public/css/vendor/bootstrap.min.css"></style>
<style src="../../public/css/vendor/owl.theme.default.min.css"></style>
<style src="../../public/css/vendor/magnific-popup.css"></style>
<style src="../../public/css/vendor/animate.min.css"></style>
<style src="../../public/css/fontawesome.css"></style>
<style src="../../public/css/brands.css"></style>
<style src="../../public/css/solid.css"></style>
<style src="../../public/css/style.css"></style>

<style>
	.col-sm-4 {
		width: 20%;
		height: 220px;
	}
	.box-image-1 {
		/* border: 1px solid black; */
		text-align: center;
		vertical-align: middle;
		height: 220px;
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
	
</style>