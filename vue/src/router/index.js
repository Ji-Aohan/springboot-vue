import Vue from 'vue'
import VueRouter from 'vue-router'
import Manager from '../views/Manager.vue'
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";
import NotFound from "@/views/404.vue"

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'manager',
    component: Manager,
    redirect: '/home',
    children: [
      { path: 'user', name: 'User', meta: { name: '用户管理' }, component: () => import('@/views/manager/User') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home') },
      { path: '403', name: 'Auth', meta: { name: '无权限访问' }, component: () => import('@/views/manager/Auth') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('@/views/manager/Person') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password') },
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/register',
    name: 'register',
    component: Register
  },
  {
    path: '/*',
    name: '404',
    meta: { name: '找不到资源' },
    component: NotFound
  },


]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  let adminPath = ['/user']
  let user = JSON.parse(localStorage.getItem('honey-user') || '{}')
  if (user.role !== '管理员' && adminPath.includes(to.path)) {
    next('/403')
  } else {
    next()
  }
})

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
