<template>
  <div>
    <div class="page">
      <div class="marvel-device nexus5">
        <div class="top-bar"></div>
        <div class="sleep"></div>
        <div class="volume"></div>
        <div class="camera"></div>
        <div class="screen">
          <div class="screen-container">
            <div class="status-bar">
              <div class="time"></div>
              <div class="battery">
                <i class="zmdi zmdi-battery"></i>
              </div>
              <div class="network">
                <i class="zmdi zmdi-network"></i>
              </div>
              <div class="wifi">
                <i class="zmdi zmdi-wifi-alt-2"></i>
              </div>
              <div class="star">
                <i class="zmdi zmdi-star"></i>
              </div>
            </div>
            <div class="chat">
              <div class="chat-container">
                <div class="user-bar">

                  <div class="name">
                    <span>Whatsapp de {{this.$route.params.userId}}</span>
                  </div>
                </div>
                <div class="content">
                  <v-list
                      subheader
                      two-line
                  >
                    <template v-for="chat in openChats">
                      <v-list-item v-if="chat.user"
                          :key="chat.user"
                          @click="navigateToChat(chat.user)"
                      >
                        <v-list-item-avatar style="margin-right: 10px">
                          <div class="avatar" >
                            <v-icon  large>mdi-account-circle</v-icon>
                          </div>
                        </v-list-item-avatar>

                        <v-list-item-content>
                          <v-list-item-title v-if="chat.user" v-text="chat.user"></v-list-item-title>

                        </v-list-item-content>

                        <v-list-item-action>
                          <v-btn icon>
                           <div v-if="chat.numberOfMessages" style="background-color: limegreen; color: white; border-radius: 50px; width: 20px; height: 20px; padding-top: 1px">
                             {{chat.numberOfMessages}}
                           </div>
                          </v-btn>
                        </v-list-item-action>
                        <br>
                      </v-list-item>
                      <v-divider v-if="chat.user" :key="chat.user" style="margin-left: 65px; margin-right: 15px"></v-divider>
                    </template>
                  </v-list>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <v-row justify="center" data-app>
      <v-dialog
          v-model="dialoguito"
          fullscreen
          hide-overlay
          transition="dialog-bottom-transition"
      >

        <v-card>
          <v-toolbar
              dark
              color="#004e45"
          >

            <v-toolbar-title>New contact</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-toolbar-items>
              <v-btn
                  dark
                  text
                  @click="dialoguito = false"
              >
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </v-toolbar-items>
          </v-toolbar>
          <v-card-title class="text-h5 grey lighten-2">
          <p style="margin-right: 10px">Name: </p>
          <v-text-field v-model="addContactValue"></v-text-field>

          </v-card-title>
          <v-btn large color="#004e45" @click="addContact()" ><p style="color: white" >Add contact</p></v-btn>
        </v-card>
      </v-dialog>
    </v-row>
    <v-btn
        @click="dialoguito = !dialoguito"
        class="mx-2"
        style="position: fixed; right: 10px; bottom: 10px"
        color="#004e45"
        fab
        dark
    >
      <v-icon dark>
        mdi-plus
      </v-icon>
    </v-btn>
  </div>
</template>
<script>
export default {
  data: () => ({
    addContactValue: '',
    dialoguito: false,
    openChats: [],
    files: [
      {
        color: 'blue',
        icon: 'mdi-clipboard-text',
        subtitle: 'Jan 20, 2014',
        title: 'Vacation itinerary',
      },
      {
        color: 'amber',
        icon: 'mdi-gesture-tap-button',
        subtitle: 'Jan 10, 2014',
        title: 'Kitchen remodel',
      },
    ],
    folders: [
      {
        subtitle: 'Jan 9, 2014',
        title: 'Photos',
      },
      {
        subtitle: 'Jan 17, 2014',
        title: 'Recipes',
      },
      {
        subtitle: 'Jan 28, 2014',
        title: 'Work',
      },
    ],
  }),
  methods: {
    navigateToChat(friendId) {
      this.$router.push("/chat/"+ this.$route.params.userId +"/"+ friendId)
    },
    addContact() {
      this.$router.push("/chat/"+ this.$route.params.userId +"/"+ this.addContactValue)
      this.addContactValue = '';
    }
  },
  created() {
    this.connection = new WebSocket("ws://localhost:7001/message?senderId="+this.$route.params.userId)
    let vm = this;
    this.connection.onmessage = function(event) {
      var chats =  JSON.parse(event.data);
      console.log('chats, c', event)

      if(chats.type == 'new-messages') {
        vm.openChats = [];
        for(var chat in chats.data) {
          if(chat != undefined) {
            let messageToPush = {user: chat, numberOfMessages: chats.data[chat].length};
            console.log(messageToPush)
            vm.openChats.push(messageToPush)
          }

        }


      }
    }


  }
}
</script>