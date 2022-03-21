function deleteFile(id) {
  if (confirm("파일을 삭제하시겠습니까?")) {
    $.ajax({
      url: "/root/support/delete",
      type: "get",
      data: {id: id},
      success: function (data) {
        $("." + id).css("display", "none");
      }
    })
  }
}