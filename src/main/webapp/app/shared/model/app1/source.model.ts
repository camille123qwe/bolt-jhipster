import dayjs from 'dayjs';

export interface ISource {
  id?: number;
  uuid?: string;
  name?: string;
  label?: string | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ISource> = {};
