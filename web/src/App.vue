<template>
  <NavBar />
  <router-view></router-view>
</template>

<script>
  import NavBar from './components/NavBar.vue'
  import "bootstrap/dist/css/bootstrap.min.css"
  import "bootstrap/dist/js/bootstrap"
  //import $ from 'jquery'

  export default{
    components: {
      NavBar
    },
    setup() {
      // $.ajax({
      //   url: "http://127.0.0.1:3000/user/account/info",
      //   type: "get",
      //   headers: {
      //     Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlNTFkODAwNWJhZWE0YjU1YjczNWEwMjJkYzE5MTQyZCIsInN1YiI6IjEiLCJpc3MiOiJzZy" + 
      //     "IsImlhdCI6MTY2MjU2NTg0MiwiZXhwIjoxNjYzNzc1NDQyfQ.9w7HaMP6G2NIF_h9S9CXztsH8uUgPcXKNSryEG5E6xY",
      //   },
      //   success(resp) {
      //     console.log(resp);
      //   },
      //   error(resp) {
      //     console.log(resp);
      //   }
      // })

      // $.ajax({
      //   url: "http://127.0.0.1:3000/user/account/register",
      //   type: "post",
      //   data: {
      //     username: "whb",
      //     password: "Aa123456",
      //     confirmedPassword: "Aa123456"
      //   },
      //   success(resp) {
      //     console.log(resp);
      //   },
      //   error(resp) {
      //     console.log(resp);
      //   }
      // })
    }
  }
</script>

<style>
  body {
    background-image: url("@/assets/images/background.jpg");
    /* 100%填充 */
    background-size: cover;  
  }
</style>