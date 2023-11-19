import * as axios from 'axios';
import { Pack, Theme, Work } from './models/business';

type PacksApi = {
  loadPacks: () => Promise<axios.AxiosResponse<Pack[]>>;
};

type ThemesApi = {
  loadThemes: () => Promise<axios.AxiosResponse<Theme[]>>;
};

type WorksApi = {
  loadWorks: () => Promise<axios.AxiosResponse<Work[]>>;
};

export type { PacksApi, ThemesApi, WorksApi };
