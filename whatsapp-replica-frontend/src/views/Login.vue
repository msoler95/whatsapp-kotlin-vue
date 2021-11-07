<template>
  <div>
    <h1  style="padding-top: 100px">Welcome to whatsapp!</h1>
      <v-card-title style="padding-top: 50px" class="text-h5 grey lighten-2">
        <p style="margin-right: 10px">Your name: </p>
        <v-text-field v-model="yourName"></v-text-field>
      </v-card-title>
      <v-btn large color="#004e45" @click="login()" ><p style="color: white" >Log in</p></v-btn>
      <p style="position: absolute; bottom: 5px; left:0; right:0; ">By msoler95</p>
  </div>
</template>
<script>
export default {
  yourName: '',
  data() {
    return {
      userId: 1,
      friendId: 2
    }
  },
  methods: {
    login() {
        this.$router.push('chats/' + this.yourName )

    }
  }
}
</script>