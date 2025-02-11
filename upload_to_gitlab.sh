#!/bin/bash

# 提示用户输入提交信息
read -p "请输入提交信息: " commit_message

# 进入项目目录，将 <your_project_path> 替换为你的实际项目路径
cd D:\桌面\bishe\xiaochengxu

# 检查当前目录是否为 Git 仓库
if [ ! -d ".git" ]; then
    echo "当前目录不是一个 Git 仓库，请检查路径。"
    exit 1
fi

# 添加所有更改到暂存区
git add .

# 提交更改，使用用户输入的提交信息
git commit -m "$commit_message"

# 推送到 GitLab 远程仓库，将 <your_remote_branch> 替换为你的实际远程分支名
git remote add origin git@github.com:217473/bishe.git

git push -u origin master

echo "代码已成功推送到 GitLab。"