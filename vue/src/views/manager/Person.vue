<template>
  <div>
    <el-card style="width: 60%">
      <div style="text-align: center">
        <el-upload
          class="avatar-uploader"
          action="http://localhost:9090/file/upload"
          :headers="{ token: user.token }"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
        >
          <img v-if="user.avatar" :src="user.avatar" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </div>
      <el-form label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="user.address"></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" size="medium" @click="handleSave">保存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
  
  <script>
export default {
  name: "Person",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("honey-user") || "{}"),
    };
  },
  created() {
  },
  methods: {
    handleAvatarSuccess(response,file,fileList) {
        this.user.avatar=response.data
    },
    handleSave(){
        //保存当前用户信息到数据库
        this.$request.put('/user/update',this.user).then(res=>{
            if(res.code==='200'){
                this.$message.success('保存成功')
                //更新浏览器缓存的用户信息
                localStorage.setItem('honey-user',JSON.stringify(this.user))
                //触发父级数据的更新
                this.$emit('update:user', this.user)
            }else{
                this.$message.error(res.msg)
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
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 50%;
}
</style>