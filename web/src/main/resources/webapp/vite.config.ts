import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
// @ts-ignore
export default defineConfig({
  build: {
    assetFileNames: 'assets',
  },
  plugins: [react()],
});
