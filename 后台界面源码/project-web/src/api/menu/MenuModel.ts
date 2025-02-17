//菜单数据类型
export type MenuType = {
    menuId: string,
    parentId: string,
    title: string,
    code: string,
    name: string,
    path: string,
    url: string,
    type: string //菜单类型，1：目录，2：菜单，3：按钮
    icon: string,
    parentName: string,
    orderNum:string
}