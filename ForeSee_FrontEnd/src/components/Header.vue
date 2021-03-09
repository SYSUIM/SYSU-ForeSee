<template>
  <div class="header header-1">
    <!-- MIDDLE BAR -->
    <div class="middlebar d-none d-sm-block">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-3 col-md-3">
            <div class="logo">
              <a href="index.html">
              <img src="../assets/images/logo-black.png" alt="" width="100%" />
              </a>
            </div>
          </div>
          
          <div class="col-9 col-md-9">
            <div class="contact-info">
              
              <!-- 互联网行业分析报告 -->
              <!-- <div class="rs-icon-1">
                <div class="icon">
                  <router-link :to="'/industryrepo'">
                    <div class="fas fa-window-restore"></div>
                  </router-link>
                </div>
                <div class="body-content">
                  <router-link :to="'/industryrepo'">
                    <div class="heading">互联网行业分析报告</div>
                  </router-link>
                </div>
              </div> -->
              <div class="rs-icon-1">
                <div class="icon">
                  <a href="index.html">
                  <div class="fas fa-home"></div>
                  </a>
                </div>
                <div class="body-content">
                  <a href="index.html">
                  <div class="heading">HOME</div>
                  </a>
                </div>
              </div>
              <!-- INFO 1 -->
              <div class="rs-icon-1">
                <div class="icon">
                    <div class="fas fa-envelope"></div>
                </div>
                <div class="body-content">
                    <div class="heading">Email Support :</div>
                    info@foresee.com
                </div>
              </div>
              <!-- INFO 2 -->
              <!-- <div class="rs-icon-1">
                <div class="icon">
                  <div class="fas fa-clock"></div>
                </div>
                <div class="body-content">
                  <div class="heading">Work Hour :</div>
                  Mon - Fri 08:00 - 17:00
                </div>
              </div> -->
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- NAVBAR SECTION -->
    <!-- <div class="navbar-main">
      <div class="container">
        <nav id="navbar-example" class="navbar navbar-expand-lg navbar-bg">
          <button
            class="navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#navbarNavDropdown"
            aria-controls="navbarNavDropdown"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav">
              <li class="nav-item dropdown">
                <a
                  class="nav-link dropdown-toggle"
                  href="#"
                  role="button"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                >
                  Home
                </a>
                <div class="dropdown-menu">
                  <a class="dropdown-item" href="index.html">Homepage 1</a>
                  <a class="dropdown-item" href="index-2.html">Homepage 2</a>
                </div>
              </li>

              <li class="nav-item">
                <a class="nav-link" href="about.html">About</a>
              </li>

              <li class="nav-item">
                <a class="nav-link" href="services.html">Services</a>
              </li>

              <li class="nav-item dropdown active">
                <a
                  class="nav-link dropdown-toggle"
                  href="#"
                  role="button"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                >
                  Companies
                </a>
                <div class="dropdown-menu">
                  <a class="dropdown-item" href="projects.html"
                    >Companies Grid</a
                  >
                  <a class="dropdown-item" href="projects-single.html"
                    >Companies Single</a
                  >
                </div>
              </li>

              <li class="nav-item dropdown">
                <a
                  class="nav-link dropdown-toggle"
                  href="#"
                  role="button"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                >
                  News
                </a>
                <div class="dropdown-menu">
                  <a class="dropdown-item" href="news.html">News Grid</a>
                  <a class="dropdown-item" href="news-single.html"
                    >Single News</a
                  >
                </div>
              </li>

              <li class="nav-item">
                <a class="nav-link" href="contact.html">Contact</a>
              </li>
            </ul>
          </div>
          <a href="#" class="btn btn-primary btn-navbar d-none d-sm-block"
            >Get a Quote</a
          >
        </nav>
      </div>
    </div> -->
  </div>
</template>

<script src="../../public/js/vendor/modernizr.min.js"></script>
<script>
export default {};
</script>
<style src="../../public/css/vendor/bootstrap.min.css"></style>

<style src="../../public/css/vendor/owl.theme.default.min.css"></style>
<style src="../../public/css/vendor/magnific-popup.css"></style>
<style src="../../public/css/vendor/animate.min.css"></style>
<style src="../../public/css/fontawesome.css"></style>
<style src="../../public/css/brands.css"></style>
<style src="../../public/css/solid.css"></style>
<style src="../../public/css/style.css"></style>

<style scoped>
  .header {
    position: fixed;
    height: 100px;
    width: 100%;
    background-color: rgba(255, 255, 255) !important;
    z-index: 3;
    /* 阴影 */
    box-shadow: 0px 7px 7px rgba(0,0,0,.3);
    transition: all .2s;
  }
</style>