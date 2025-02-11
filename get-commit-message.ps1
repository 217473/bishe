# get-commit-message.ps1
Add-Type -AssemblyName Microsoft.VisualBasic
$commitMessage = [Microsoft.VisualBasic.Interaction]::InputBox("请输入提交信息:", "提交到 GitLab", "Auto-commit")
if ($commitMessage -ne "") {
    echo $commitMessage
} else {
    echo "用户取消操作"
    exit 1
}
