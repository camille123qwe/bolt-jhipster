import dayjs from 'dayjs';
import { DeviceRole } from 'app/shared/model/enumerations/device-role.model';

export interface IDevice {
  id?: number;
  uuid?: string;
  name?: string;
  label?: string | null;
  description?: string | null;
  role?: keyof typeof DeviceRole | null;
  createdAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IDevice> = {};
