<template>
  <div>
    <el-card style="width: 50%">
      <div style="margin-right: 30px">
        <el-form label-width="80px" :rules="rules" :model="user" ref="formRef">
          <el-form-item label="原密码" prop="password">
            <el-input v-model="user.password" disabled></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="user.newPassword" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="user.confirmPassword" show-password></el-input>
          </el-form-item>
          <div style="text-align: center">
            <el-button type="primary" size="medium" @click="confirmUpdate">确认修改</el-button>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
  
  <script>
export default {
  name: "Password",
  data() {
    const checkPassword = (rule, value, callback) => {
      if (value==='') {
        callback(new Error("密码不能为空"));
      } else if (value !== this.user.newPassword) {
        callback(new Error("两次输入的密码不一致"));
      }else{
        callback()
      }
    };
    return {
      user: JSON.parse(localStorage.getItem("honey-user") || "{}"),
      rules: {
        password: [
          { required: true, message: "请输入原密码", trigger: "blur" },
        ],
        newPassword: [
          { required: true, message: "请输入新密码", trigger: "blur" },
        ],
        confirmPassword: [
          {
            validator: checkPassword,
            required: true,
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {},
  methods: {
    confirmUpdate(){
        this.$refs.formRef.validate((valid) => {
            if(valid){
                this.user.password=this.user.newPassword
                this.$request.put('/user/update',this.user).then(res=>{
                   if(res.code==='200'){
                    this.$message.success('修改密码成功')
                    //重新登录
                    this.$router.push('/login')
                   }else{
                    this.$message.error(res.msg)
                   } 
                })
            }
        })
    }
  },
};
</script>
  
  <style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
</style>