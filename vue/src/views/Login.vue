<template>
  <div
    style="
      height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: azure;
    "
  >
    <div style="flex: 1">
      <img src="@/assets/login.png" alt="" style="width: 100%" />
    </div>
    <div
      style="
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
      "
    >
      <el-form :model="user" label-width="100px" :rules="rules" ref="loginRef">
        <div
          style="
            font-size: 20px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
          "
        >
          欢迎登录后台管理系统
        </div>
        <el-form-item label="账号:" prop="username">
          <el-input placeholder="请输入账号" prefix-icon="el-icon-user" size="medium" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item label="密码:" prop="password">
          <el-input
            placeholder="请输入密码"
            show-password
            v-model="user.password"
          ></el-input>
        </el-form-item>
        <el-form-item label="验证码:" prop="code">
          <el-input placeholder="请输入验证码" v-model="user.code"></el-input>
          <valid-code-vue @update:value="getCode" />
        </el-form-item>
        <div style="align-items: center">
          <el-form-item>
            <el-button size="big" type="primary" @click="login">登录</el-button>
            <el-button size="big" type="success" @click="$router.push('/register')">注册</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import ValidCodeVue from "@/components/ValidCode.vue";
export default {
  name: "Login",
  components: {
    ValidCodeVue,
  },
  data() {
    const validateCode = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入验证码"));
      } else if (value.toLowerCase() !== this.code) {
        callback(new Error("验证码错误"));
      } else {
        callback();
      }
    };
    return {
      code: "", //验证码组件传递过来的code
      user: {
        code: "", // 用户输入的code验证码
        username: "",
        password: "",
      },
      rules: {
        username: [{ required: true, message: "账号不为空", trigger: "blur" }],
        password: [{ required: true, message: "密码不为空", trigger: "blur" }],
        code: [{ validator: validateCode, trigger: "blur" }]
      }
    };
  },
  created() {
    
  },
  methods: {
    getCode(code) {
      this.code = code.toLowerCase();
    },
    login() {
      this.$refs['loginRef'].validate((valid) => {
        if (valid) {
          //success
          this.$request.post("/login", this.user).then((res) => {
            if (res.code === "200") {
              this.$router.push("/");
              this.$message.success("登录成功");
              localStorage.setItem("honey-user", JSON.stringify(res.data)); //存储用户数据
            } else {
              this.$message.error(res.msg);
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>

<style scoped>
</style>
