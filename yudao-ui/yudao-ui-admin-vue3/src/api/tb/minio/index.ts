import request from '@/config/axios'

export const MinioApi = {
  wpsVba:"http://1.119.159.138:48083/minio/common-file/wps2019vba.exe",
  importPrepareTemplate:"http://1.119.159.138:48083/minio/screen-ppd/%E5%BE%85%E7%AD%9B%E6%9F%A5%E4%BA%BA%E5%91%98%E6%A8%A1%E6%9D%BF-%E5%A4%9A%E9%80%89.xls",
  importRealTemplate:"http://1.119.159.138:48083/minio/screen-ppd/%E6%91%B8%E5%BA%95%E4%BA%BA%E5%91%98%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF-%E5%A4%9A%E9%80%89.xls",
  // 查询报表数据
  getPresignedUrl: async (data: any) => {
    return await request.post({url: `tb/minioFile/api/getPresignedUrl`, data})
  },
  // 查询报表数据
  downloadFile: async (name,bucket) => {
    return await request.downloadPost({url: `tb/minioFile/down/${name}/${bucket}`})
  }
}
