<template>
  <div class="after-sale-list">
    <el-card>
      <template #header>
        <span>售后管理</span>
      </template>

      <el-table :data="afterSaleList" border stripe>
        <el-table-column prop="afterSaleNo" label="售后单号" width="180" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            {{ row.type === 'REFUND' ? '退款' : '退货退款' }}
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" min-width="200" />
        <el-table-column prop="refundAmount" label="退款金额" width="100">
          <template #default="{ row }">
            ¥{{ row.refundAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="160" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button 
              v-if="row.status === 'PENDING'" 
              size="small" 
              type="success"
              @click="handleApprove(row)"
            >
              同意
            </el-button>
            <el-button 
              v-if="row.status === 'PENDING'" 
              size="small" 
              type="danger"
              @click="handleReject(row)"
            >
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const afterSaleList = ref([])

const handleView = (row) => {
  ElMessage.info('查看售后详情：' + row.afterSaleNo)
}

const handleApprove = (row) => {
  ElMessageBox.confirm('确认同意该售后申请？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('操作成功')
  })
}

const handleReject = (row) => {
  ElMessageBox.prompt('请输入拒绝原因', '拒绝售后', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '请输入拒绝原因'
  }).then(({ value }) => {
    ElMessage.success('操作成功')
  })
}

const getStatusType = (status) => {
  const typeMap = {
    'PENDING': 'warning',
    'APPROVED': 'success',
    'REJECTED': 'danger',
    'REFUNDED': 'info'
  }
  return typeMap[status] || 'info'
}
</script>

