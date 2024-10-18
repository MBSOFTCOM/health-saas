import request from '@/config/axios'

export const MinioApi = {
  wpsVba:"http://1.119.159.138:48083/minio/common-file/wps2019vba.exe",
  // 查询报表数据
  getPresignedUrl: async (data: any) => {
    return await request.post({url: `tb/minioFile/api/getPresignedUrl`, data})
  },
  // 查询报表数据
  downloadFile: async (name,bucket) => {
    return await request.downloadPost({url: `tb/minioFile/down/${name}/${bucket}`})
  }
}
