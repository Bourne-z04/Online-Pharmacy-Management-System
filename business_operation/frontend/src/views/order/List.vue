<template>
  <div class="order-list">
    <el-card>
      <template #header>
        <span>订单列表</span>
      </template>

      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="全部订单" name="all" />
        <el-tab-pane label="待发货" name="PAID" />
        <el-tab-pane label="配送中" name="SHIPPING" />
        <el-tab-pane label="已完成" name="COMPLETED" />
        <el-tab-pane label="售后中" name="AFTER_SALE" />
      </el-tabs>

      <el-table :data="orderList" border stripe>
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="receiverName" label="收货人" width="100" />
        <el-table-column prop="receiverPhone" label="联系电话" width="120" />
        <el-table-column prop="totalQuantity" label="商品数量" width="100" />
        <el-table-column prop="payableAmount" label="订单金额" width="100">
          <template #default="{ row }">
            ¥{{ row.payableAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="160" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button 
              v-if="row.status === 'PAID'" 
              size="small" 
              type="primary"
              @click="handleShip(row)"
            >
              发货
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('all')
const orderList = ref([])

const handleTabClick = () => {
  ElMessage.info('加载订单列表')
}

const handleView = (row) => {
  ElMessage.info('查看订单详情：' + row.orderNo)
}

const handleShip = (row) => {
  ElMessage.info('发货功能待实现')
}

const getStatusType = (status) => {
  const typeMap = {
    'PAYING': 'warning',
    'PAID': 'primary',
    'SHIPPING': 'success',
    'COMPLETED': 'info',
    'CANCELLED': 'danger',
    'AFTER_SALE': 'warning'
  }
  return typeMap[status] || 'info'
}
</script>

