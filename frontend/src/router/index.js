import { createWebHistory, createRouter } from 'vue-router'
/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index.vue')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: 'Home', icon: 'home', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/doc',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/agileInnovatorsDoc.vue'),
        name: 'Doc',
        meta: { title: 'Agile Innovators doc', icon: 'documentation', affix: false  }
      }
    ]
  },
  {
    path: '/noticee',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/noticee/index.vue'),
        name: 'Noticee',
      }
    ]
  },
  {
    path: '/dashboard',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/dashboard/index.vue'),
        name: 'Dashboard',
        meta: {
          title: 'Dashboard',
          icon: 'dashboard',
        }
      }
    ]
  },
  {
    path: '/project',
    component: Layout,
    hidden: false,
    children: [
      {
        path: 'index',
        component: () => import('@/views/project/project/index.vue'),
        name: 'ProjectList',
        meta: { title: 'Projects', icon: 'project', affix: false }
      },
      {
        path: 'detail/:id',
        redirect: to => {
          return `/project/detail/${to.params.id}/overview`
        },
        component: () => import('@/views/project/project/detail.vue'),
        name: 'ProjectDetail',
        hidden: true,
        meta: { title: '项目详情' },
        props: true ,
        children: [ {
          path: 'overview',
          component: () => import('@/views/project/project/overview.vue'),
          name: 'ProjectOverview',
          meta: { title: '项目概览' }
        },
          {
            path: 'plan',
            component: () => import('@/views/project/project/plan.vue'),
            name: 'ProjectPlan',
            meta: { title: '项目规划' }
          },
          {
            path: 'requirements',
            component: () => import('@/views/project/project/requirements.vue'),
            name: 'ProjectRequirements',
            meta: { title: '需求管理' }
          },
          {
            path: 'defects',
            component: () => import('@/views/project/project/defects.vue'),
            name: 'ProjectDefects',
            meta: { title: '缺陷管理' }
          },
          {
            path: 'tasks',
            component: () => import('@/views/project/project/tasks.vue'),
            name: 'ProjectTasks',
            meta: { title: '工作项' }
          },
          {
            path: 'iterations',
            component: () => import('@/views/project/project/iterations.vue'),
            name: 'ProjectIterations',
            meta: { title: '迭代管理' }
          },
          {
            path: 'releases',
            component: () => import('@/views/project/project/releases.vue'),
            name: 'ProjectReleases',
            meta: { title: '发布管理' }
          },
          {
            path: 'baseline',
            component: () => import('@/views/project/project/baseline.vue'),
            name: 'ProjectBaseline',
            meta: { title: '基线管理' }
          },
          {
            path: 'reviews',
            component: () => import('@/views/project/project/reviews.vue'),
            name: 'ProjectReviews',
            meta: { title: '评审管理' }
          },
          {
            path: 'testing',
            component: () => import('@/views/project/project/testing.vue'),
            name: 'ProjectTesting',
            meta: { title: '测试管理' }
          },
          {
            path: 'documents',
            component: () => import('@/views/project/project/documents.vue'),
            name: 'ProjectDocuments',
            meta: { title: '文档管理' }
          },
          {
            path: 'resources',
            component: () => import('@/views/project/project/resources.vue'),
            name: 'ProjectResources',
            meta: { title: '资源管理' }
          },
          {
            path: 'reports',
            component: () => import('@/views/project/project/reports.vue'),
            name: 'ProjectReports',
            meta: { title: '统计报表' }
          }
        ]
      }
    ]
  },


]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole'),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser'),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict' }
      }
    ]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: () => import('@/views/monitor/job/log'),
        name: 'JobLog',
        meta: { title: '调度日志', activeMenu: '/monitor/job' }
      }
    ]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/views/tool/gen/editTable'),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
});

export default router;
