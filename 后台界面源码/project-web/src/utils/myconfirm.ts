/**
 * 信息确定提示框 
 */
import { ElMessageBox } from 'element-plus'
export default function myconfirm(test: string) {
    return new Promise((resolve, reject) => {
        ElMessageBox.confirm(test, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
        ).then(() => {//确定按钮
            resolve (true)
        }).catch(() => {//取消按钮
            reject (false)
        })
    }).catch(() => {//关闭按钮
        return false
    });
     
}