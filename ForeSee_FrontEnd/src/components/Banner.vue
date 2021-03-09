<template>
  <!-- BANNER -->
  <!-- <div
    class="section banner-page"
    data-background="../assets/images/banner-bg.png"
  > -->
  <div class="section banner-page">
    <div class="content-wrap pos-relative">
      <div class="container">
        <div class="col-12 col-md-12">
          <div class="d-flex bd-highlight mb-2">
            <div class="title-page">{{ title }}</div>
          </div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
              <!-- <li class="breadcrumb-item"></li> -->
              <li class="breadcrumb-item active" aria-current="page">{{ show_title }}{{ show_value }}</li>
              <!-- <li class="breadcrumb-item active" aria-current="page"></li> -->
            </ol>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>
<script src="../../public/js/vendor/modernizr.min.js"></script>

<script>
export default {
  props: ['title','show_title','show_value']
};
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
.banner-page:before {
  /* background-image: url('../assets/images/banner-bg.png'); */
  opacity: 0.0;
}

</style>
<style scoped>
    .banner-page {
      padding-top: 50px;
    }
</style>