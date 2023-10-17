<template>
  <div>
    <el-table :data="userData">
      <el-table-column
        v-for="(val, index) in userColoms"
        :key="index"
        :label="val"
        :prop="index"
      >
      </el-table-column>
      <el-table-column label="头像">
        <template v-slot="scope">
          <el-image
          v-if="scope.row.avatar"
            style="width: 50px; height: 50px; border-radius: 50%;"
            :src="scope.row.avatar"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作">
          <el-button type="primary" size="mini">编辑</el-button>
          <el-button type="danger" size="mini">删除</el-button>
      </el-table-column>
    </el-table>
  </div>
</template>
  
  <script>
export default {
  name: "User",
  data() {
    return {
      userData: [],
      userColoms: {
        id: "id",
        username: "用户名",
        name: "姓名",
        email: "邮箱",
        address: "地址",
      },
    };
  },
  created() {
    this.$request.get("/user/selectAll").then((res) => {
      this.userData = res.data;
    });
  },
  methods: {},
};
</script>
  
  <style scoped>
</style>