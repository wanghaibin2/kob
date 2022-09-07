import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RankListIndexView from '../views/ranklist/RankListIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFound from '../views/error/NotFound'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/index"
  },
  {
    path: "/pk/index",
    name: "pk_index", 
    component: PkIndexView
  },
  {
    path: "/record/index",
    name: "record_index", 
    component: RecordIndexView
  },
  {
    path: "/ranklist/index",
    name: "ranklist_index", 
    component: RankListIndexView
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginView
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView
  },
  {
    path: "/user/bot/index",
    name: "user_bot_index", 
    component: UserBotIndexView
  },
  {
    path: "/404",
    name: "not_found", 
    component: NotFound
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
