<template>
  <div>
    <el-container>
      <el-aside :width="asideWidth">
        <div
          style="
            height: 60px;
            line-height: 60px;
            font-size: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
          "
        >
          <img src="@/assets/logo1.png" style="width: 30px" alt="" />
          <span class="logo-title" v-show="!isCollapse">Honey2024</span>
        </div>
        <el-menu
          router
          :collapse="isCollapse"
          :collapse-transition="false"
          background-color="#001529"
          active-text-color="#fff"
          text-color="rgba(255, 255, 255, 0.65)"
          :default-active="$route.path"
          style="border: none"
        >
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-submenu index="2">
            <template slot="title"
              ><i class="el-icon-menu"></i><span>信息管理</span></template
            >
            <el-menu-item index="/user">用户管理</el-menu-item>
            <el-menu-item index="/admin">管理员管理</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header
          style="
            height: 60px;
            line-height: 60px;
            display: flex;
            align-items: center;
            box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
          "
        >
          <i
            :class="collapseIcon"
            @click="handleCollapse"
            style="font-size: 26px"
          ></i>
          <el-breadcrumb separator="/" style="margin-left: 20px">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/' }"
              >课程管理</el-breadcrumb-item
            >
          </el-breadcrumb>

          <div
            style="
              flex: 1;
              display: flex;
              justify-content: flex-end;
              align-items: center;
            "
          >
            <i
              class="el-icon-quanping"
              @click="handleFullScreen"
              style="font-size: 25px"
            ></i>
            <el-dropdown placement="bottom">
              <div style="display: flex; align-items: center; cursor: pointer">
                <img
                  src="@/assets/logo1.png"
                  alt=""
                  style="width: 40px; height: 40px; margin: 0 5px"
                />
                <span>管理员</span>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="logout"
                  >退出登录</el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <el-main>
          <el-table
            :data="users"
            stripe
            :header-cell-style="{
              backgroundColor: 'aliceblue',
              fontWeight: 'bold',
              color: '#333',
            }"
          >
            <el-table-column
              label="用户名"
              prop="username"
              align="center"
            ></el-table-column>
            <el-table-column
              label="姓名"
              prop="name"
              align="center"
            ></el-table-column>
            <el-table-column
              label="邮箱"
              prop="email"
              align="center"
            ></el-table-column>
            <el-table-column
              label="地址"
              prop="address"
              align="center"
            ></el-table-column>
            <el-table-column label="文件上传" align="center">
              <template v-slot="scope">
                <el-upload
                  action="http://localhost:9090/file/upload"
                  :headers="{ token: user.token }"
                  :show-file-list="false"
                  :on-success="(row,file,fileList)=>handleTableFileUpload(scope.row,file,fileList)"
                  list-type="picture"
                >
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </template>
            </el-table-column>
            <el-table-column label="预览" align="center">
              <template v-slot="scope">
              <el-image
                v-if="scope.row.avatar"
                style="width: 50px; height: 50px"
                :src="scope.row.avatar"
              ></el-image>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "HomeView",
  data() {
    return {
      isCollapse: false,
      asideWidth: "200px",
      collapseIcon: "el-icon-s-fold",
      users: [],
      user: JSON.parse(localStorage.getItem("honey-user") || "{}"),
      url: ''
    };
  },
  mounted() {
    this.$request.get("/user/selectAll").then((res) => {
      this.users = res.data;
    });
  },
  methods: {
    handleFileUpload(response, file, fileList) {
      console.log(response, file, fileList);
    },
    handleTableFileUpload(row, file, fileList) {
      console.log(row, file, fileList);
      row.avatar = file.response.data;
      // this.$set(row, 'avatar', file.response.data)
      console.log(row);
      // 触发更新就可以了
      request.put("/user/update", row).then((res) => {
        if (res.code === "200") {
          this.$message.success("上传成功");
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    handleFullScreen() {
      document.documentElement.requestFullscreen();
    },
    handleCollapse() {
      this.isCollapse = !this.isCollapse;
      this.asideWidth = this.isCollapse ? "64px" : "200px";
      this.collapseIcon = this.isCollapse
        ? "el-icon-s-unfold"
        : "el-icon-s-fold";
    },
    logout() {
      localStorage.removeItem("honey-user");
      this.$router.push("/login");
    },
  },
};
</script>

<style>
.el-menu--inline,
.el-menu-item {
  background-color: #000c17 !important;
}

.el-submenu__title {
  height: 40px !important;
  line-height: 40px !important;
  padding: 0 25px !important;
  transition: color 0s;
}

.el-menu--collapse .el-submenu__title {
  padding: 0 20px !important;
}

.el-submenu__title > i:nth-child(1) {
  margin-top: 2px;
}

.el-submenu__title > i.el-submenu__icon-arrow {
  margin-top: -5px;
}

.el-menu-item {
  min-width: 0 !important;
  width: calc(100% - 10px);
  margin: 5px;
  height: 40px !important;
  line-height: 40px !important;
  border-radius: 5px;
}

.el-menu--inline > .el-menu-item {
  padding-left: 50px !important;
}

.el-menu-item.is-active {
  background-color: dodgerblue !important;
}

.el-menu-item:hover {
  color: #fff !important;
}

.el-submenu__title:hover *,
.el-submenu__title:hover {
  color: #fff !important;
}

.el-aside {
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  background-color: #001529;
  color: #fff;
  min-height: 100vh;
  transition: width 0.3s;
}

.el-menu--collapse .el-tooltip {
  padding: 0 15px !important;
}

.logo-title {
  margin-left: 5px;
  transition: all 0.3s;
}

.el-menu {
  transition: all 0.3s;
}
</style>
