<template>
  <div>
    <v-text-field v-model="userId"></v-text-field>
    <v-text-field v-model="friendId"></v-text-field>
    <v-btn @click="startChat()">send</v-btn>
  </div>


</template>
<script>
export default {
  name: 'Login',
  data() {
    return {
      userId: 1,
      friendId: 2
    }
  },
  methods: {
    startChat() {
      if(this.userId != '' && this.friendId != '') {
        this.$router.push({ name: 'Chat', params: { userId: this.userId, friendId: this.friendId } })
      }
    }
  }
}
</script>