<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="日期" prop="showDate">
        <el-date-picker clearable v-model="queryParams.showDate" type="date" value-format="yyyy-MM-dd"
                        placeholder="请选择日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['admin:showcase:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPermi="['admin:showcase:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                   v-hasPermi="['admin:showcase:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   v-hasPermi="['admin:showcase:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="showcaseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键" align="center" prop="id"/>
      <el-table-column label="标题" align="center" prop="title"/>
      <el-table-column label="日期" align="center" prop="showDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.showDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description" show-overflow-tooltip/>
      <el-table-column label="图片" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 60px; height: 60px"
            :src="scope.row.imageUrl"
            fit="cover"
            :preview-src-list="[scope.row.imageUrl]"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['admin:showcase:edit']"
          >修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['admin:showcase:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList"
    />

    <!-- 新增/修改弹窗 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题"/>
        </el-form-item>
        <el-form-item label="日期" prop="showDate">
          <el-date-picker clearable v-model="form.showDate" type="date" value-format="yyyy-MM-dd"
                          placeholder="请选择日期"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            class="avatar-uploader"
            :action="uploadAction"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <el-input v-show="false" v-model="form.imageUrl"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listShowcase, getShowcase, delShowcase, addShowcase, updateShowcase } from '@/api/admin/showcase';
import { getToken } from '@/utils/auth';

export default {
  name: 'Showcase',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      showcaseList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        showDate: null,
        description: null,
        imageUrl: null,
        isDeleted: 0
      },
      form: {},
      rules: {
        title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
        showDate: [{ required: true, message: '日期不能为空', trigger: 'blur' }],
        imageUrl: [{ required: true, message: '图片不能为空', trigger: 'blur' }]
      },
      uploadAction: process.env.VUE_APP_BASE_API + '/common/upload',
      uploadHeaders: { Authorization: 'Bearer ' + getToken() },
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listShowcase(this.queryParams).then(response => {
        this.showcaseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { id: null, title: null, showDate: null, description: null, imageUrl: null, isDeleted: 0 }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加团队风采'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getShowcase(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改团队风采'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateShowcase(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addShowcase(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据？').then(() => {
        return delShowcase(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      })
    },
    handleExport() {
      this.download('admin/showcase/export', { ...this.queryParams }, `showcase_${new Date().getTime()}.xlsx`)
    },
    /** 上传成功回调 */
    handleUploadSuccess(res) {
      if (res.code === 200 && res.url) {
        this.form.imageUrl = res.url;   // 仅此一行
      } else {
        this.$message.error(res.msg || '上传失败');
      }
    },
    /** 上传前校验 */
    beforeUpload(file) {
      // const isImg = file.type.startsWith('image/')
      // const isLt2M = file.size / 1024 / 1024 < 2
      // if (!isImg) this.$message.error('只能上传图片')
      // if (!isLt2M) this.$message.error('图片大小不能超过 2MB')
      // return isImg && isLt2M
    }
  }
}
</script>

<style scoped>
.avatar-uploader >>> .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}

.avatar {
  max-width: 120px;
  max-height: 120px;
  display: block;
}
</style>
