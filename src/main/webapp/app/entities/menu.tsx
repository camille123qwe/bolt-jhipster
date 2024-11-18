import React, { useEffect } from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate } from 'react-jhipster';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const EntitiesMenu = () => {
  const lastChange = useAppSelector(state => state.locale.lastChange);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(addTranslationSourcePrefix('services/app1/'));
  }, [lastChange]);

  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/app1/project">
        <Translate contentKey="global.menu.entities.app1Project" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app1/device">
        <Translate contentKey="global.menu.entities.app1Device" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app1/strategy">
        <Translate contentKey="global.menu.entities.app1Strategy" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app1/execution-type">
        <Translate contentKey="global.menu.entities.app1ExecutionType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app1/source">
        <Translate contentKey="global.menu.entities.app1Source" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app1/customer">
        <Translate contentKey="global.menu.entities.app1Customer" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
