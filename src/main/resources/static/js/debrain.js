function deleteFile(id) {
  if (confirm("파일을 삭제하시겠습니까?")) {
    $.ajax({
      url: "/root/support/file/" + id,
      type: "get",
      success: function (data) {
        $("." + id).css("display", "none");
      }
    })
  }
}