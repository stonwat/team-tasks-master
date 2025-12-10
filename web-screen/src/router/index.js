import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    component: () => import('../views/Index.vue')
  },
  {
    path: '/power',
    name: 'power',
    component: () => import('../views/power/Power.vue')
  },
  {
    path: '/platform',
    name: 'platform',
    component: () => import('../views/platform/Platform.vue')
  },
  {
    path: '/model',
    name: 'model',
    component: () => import('../views/model/Model.vue')
  },
  {
    path: '/data',
    name: 'data',
    component: () => import('../views/data/Data.vue')
  },
  {
    path: '/agent',
    name: 'agent',
    component: () => import('../views/agent/Agent.vue')
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/admin/Admin.vue')
  }]
const router = new VueRouter({
  routes
})

export default router