import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src') // 直接设置 '@' 别名指向 src 目录
    }
  },
  server: {
    host: '0.0.0.0',
    port: 8081,
    hmr: true,
    open: true,
    // proxy: {
    //   '/api': {
    //     target: 'http://localhost:8089', // 后端地址
    //     changeOrigin: true,
    //     rewrite: (path) => path.replace(/^\/api/, '')
    //   }
    // }
  }
});