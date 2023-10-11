<template>
  <div
    style="
      height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: rgb(2, 244, 208);
    "
  >
    <div style="flex: 1">
      <img src="@/assets/register.png" alt="" style="width: 100%" />
    </div>
    <div
      style="
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
      "
    >
    <el-form :model="formData" :rules="formRules" ref="registrationForm">
      <div
          style="
            font-size: 20px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
          "
          class="registration-form"
        >
          欢迎注册
        </div>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="formData.password"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input type="password" v-model="formData.confirmPassword"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="big" @click="submitForm">确认注册</el-button>
      </el-form-item>
    </el-form>
      
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        username: '',
        password: '',
        confirmPassword: '',
      },
      formRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          // 可以添加其他用户名校验规则
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          // 可以添加其他密码校验规则
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' },
        ],
      },
    };
  },
  methods: {
    submitForm() {
      this.$refs.registrationForm.validate(valid => {
        if (valid) {
          // 表单校验通过，可以提交表单数据
          this.$request.post('/register',this.formData).then(res=>{
              if(res.code==='200'){
                  this.$router.push('/login')
                  this.$message.success("注册成功")
              }else{
                this.$message.error(res.msg)
              }
          })
        } else {
          // 表单校验不通过，不执行提交操作
        }
      });
    },
    validateConfirmPassword(rule, value, callback) {
      if (value === this.formData.password) {
        callback();
      } else {
        callback(new Error('两次输入的密码不一致'));
      }
    },
  },
};
</script>

<style scoped>
/* 添加样式 */
.registration-form {
  width: 300px;
  margin: 0 auto;
  padding: 20px;
}
</style>


