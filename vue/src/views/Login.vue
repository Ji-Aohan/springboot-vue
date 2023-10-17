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
          <el-input
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
            size="medium"
            v-model="user.username"
          ></el-input>
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
            <el-button
              size="big"
              type="success"
              @click="$router.push('/register')"
              >注册</el-button
            >
          </el-form-item>
        </div>
        <div style="text-align: right">
          <el-button type="text" @click="forgetPassword">忘记密码</el-button>
        </div>
      </el-form>
    </div>

    <el-dialog title="忘记密码" :visible.sync="formVisible" :before-close="handleClose">
      <el-form label-width="100px" :model="userForm">
        <el-form-item label="用户名:">
          <el-input v-model="userForm.username"></el-input>
        </el-form-item>
        <el-form-item label="手机号:">
          <el-input v-model="userForm.phone"></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="danger" @click="handleReset">确认重置</el-button>
          <el-button type="primary" @click="handleCancel">取消</el-button>
        </div>
      </el-form>
    </el-dialog>
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
      userForm: {},
      formVisible: false,
      code: "", //验证码组件传递过来的code
      user: {
        code: "", // 用户输入的code验证码
        username: "",
        password: "",
      },
      rules: {
        username: [{ required: true, message: "账号不为空", trigger: "blur" }],
        password: [{ required: true, message: "密码不为空", trigger: "blur" }],
        code: [{ validator: validateCode, trigger: "blur" }],
      },
    };
  },
  created() {},
  methods: {
    handleReset() {
      this.$request.put('/password',this.userForm).then(res=>{
        if(res.code==='200'){
          this.$message.success('重置成功')
          this.formVisible=false
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleClose() {
      this.userForm = {};
      this.formVisible=false
    },
    handleCancel() {
      this.userForm = {};
      this.formVisible = false;
    },
    forgetPassword() {
      this.formVisible = true;
    },
    getCode(code) {
      this.code = code.toLowerCase();
    },
    login() {
      this.$refs["loginRef"].validate((valid) => {
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
