import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../views/Login";
import Chat from "../views/Chat";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/chat',
    name: 'Chat',
    component: Chat
  },
  {
    path: '*',
    redirect: '/login',
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
