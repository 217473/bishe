import { DialogModel } from "@/type/BaseType";
import { reactive } from "vue";

export default function useDialog() {
    const dialog = reactive<DialogModel>({
        title: '标题',
        height: 280,
        width: 630,
        visible: false
    })
    //展示
    const onShow = () => {
        dialog.visible = true;
    }

    //关闭
    const onClose = () => {
        dialog.visible = false;
    }

    //确定
    const onConfirm = () => {
        dialog.visible = false;
    }
    return {
        dialog,
        onShow,
        onClose,
        onConfirm
    }
}